package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.exception.AttributeFormatException;
import de.tu_berlin.cit.intercloud.client.exception.MissingClassificationException;
import de.tu_berlin.cit.intercloud.client.exception.ParameterFormatException;
import de.tu_berlin.cit.intercloud.client.exception.UnsupportedMethodException;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.action.convert.ActionModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.action.convert.ActionModelConverter;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.representation.impl.RepresentationModelRegistry;
import de.tu_berlin.cit.intercloud.client.profiling.api.IProfilingCommand;
import de.tu_berlin.cit.intercloud.client.profiling.impl.ProfilingService;
import de.tu_berlin.cit.intercloud.client.service.api.IIntercloudClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciMethodInvocation;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.rest.ActionDocument;
import de.tu_berlin.cit.rwx4j.rest.RestDocument;
import de.tu_berlin.cit.rwx4j.xwadl.MethodDocument;
import de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument;
import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IntercloudClient implements IIntercloudClient {
    private static final Logger logger = LoggerFactory.getLogger(IntercloudClient.class);

    private final RepresentationModelRegistry representationModelRegistry = RepresentationModelRegistry.getInstance();
    private final IXmppService xmppService;
    private final OcciClient occiClient;
    private final XmppURI uri;
    private final LoggingModel loggingModel;

    public IntercloudClient(IXmppService xmppService, XwadlDocument xwadl, XmppURI uri) {
        this.xmppService = xmppService;
        this.occiClient = new OcciClient(xwadl);
        this.uri = uri;
        this.loggingModel = new LoggingModel();
        this.loggingModel.setXwad(xwadl);
    }

    @Override
    public LoggingModel getLoggingModel() {
        return this.loggingModel;
    }

    @Override
    public List<MethodModel> getMethods() {
        List<MethodModel> result = new ArrayList<>();
        MethodDocument.Method[] methodArray = occiClient.getXwadlDocument().getXwadl().getMethodArray();
        if (null != methodArray) {
            for (MethodDocument.Method m : methodArray) {
                result.add(new MethodModel(this.uri, m));
            }
        }
        return result;
    }

    @Override
    public IRepresentationModel getRepresentationModel(MethodModel methodModel) throws UnsupportedMethodException, MissingClassificationException {
        if (null == methodModel.getRequestMediaType()) {
            // do nothing --> return null
            return null;
        }
        IRepresentationModelPlugin modelPlugin = representationModelRegistry.getPlugin(methodModel.getRequestMediaType());
        if (null == modelPlugin) {
            throw new UnsupportedMethodException("The request media type is not supported.");
        }

        return ProfilingService.getInstance().execute(
                new IProfilingCommand<IRepresentationModel>() {
                    @Override
                    public String getIdentifier() {
                        return "getRequestModel";
                    }

                    @Override
                    public IRepresentationModel execute() {
                        return modelPlugin.getRequestModel(methodModel.getReference().getRequest(),
                                occiClient.getXwadlDocument().getXwadl().getGrammars());
                    }
                }
        );
    }

    @Override
    public IRepresentationModel executeMethod(MethodModel methodModel, IRepresentationModel requestRepresentationModel)
            throws XMPPException, IOException, SmackException, AttributeFormatException, XmlException, UnsupportedMethodException {
        MethodDocument.Method methodDocument = methodModel.getReference();

        // Request: RepresentationModel --> ResourceDocument (rest xml)
        OcciMethodInvocation methodInvocation = occiClient.buildMethodInvocation(methodDocument);
        if (null == requestRepresentationModel && null == methodModel.getRequestMediaType()) {
            // do nothing
        } else {
            IRepresentationModelPlugin modelPlugin = representationModelRegistry.getPlugin(methodModel.getRequestMediaType());
            if (null != modelPlugin) {
                String representationString = ProfilingService.getInstance().execute(new IProfilingCommand<String>() {
                    @Override
                    public String getIdentifier() {
                        return "getRepresentationString";
                    }

                    @Override
                    public String execute() {
                        return modelPlugin.getRepresentationString(requestRepresentationModel);
                    }
                });
                methodInvocation.setRequestRepresentation(representationString);
            } else {
                throw new UnsupportedMethodException("Cannot execute method: method not supported. " + methodModel);
            }
        }

        loggingModel.setRestRequest(methodInvocation.getXmlDocument());
        RestDocument response = xmppService.sendRestDocument(this.uri, methodInvocation.getXmlDocument());
        loggingModel.setRestResponse(response);
        // Response: ResourceDocument (rest xml) --> RepresentationModel
        IRepresentationModel representationModel = null;
        if (response.getRest().isSetMethod()
                && response.getRest().getMethod().isSetResponse()) {
            String responseMediaType = response.getRest().getMethod().getResponse().getMediaType();
            IRepresentationModelPlugin modelPlugin = representationModelRegistry.getPlugin(responseMediaType);
            if (null != modelPlugin) {
                representationModel = ProfilingService.getInstance().execute(new IProfilingCommand<IRepresentationModel>() {
                    @Override
                    public String getIdentifier() {
                        return "getResponseModel";
                    }

                    @Override
                    public IRepresentationModel execute() {
                        return modelPlugin.getResponseModel(response.getRest().getMethod().getResponse(),
                                occiClient.getXwadlDocument().getXwadl().getGrammars());
                    }
                });
            }
        }

        return representationModel;
    }

    @Override
    public List<ActionModel> getActions() {
        return ActionModelBuilder.buildActionModels(occiClient.getXwadlDocument());
    }

    @Override
    public List<ParameterModel> getParameters(ActionModel actionModel) {
        return ActionModelBuilder.buildParameterModelList(actionModel.getReference());
    }

    @Override
    public ParameterModel executeAction(ActionModel actionModel, List<ParameterModel> parameterModelList) throws ParameterFormatException, XMPPException, IOException, SmackException {
        RestDocument restDocument = RestDocument.Factory.newInstance();
        RestDocument.Rest rest = restDocument.addNewRest();
        rest.setPath(this.uri.getPath());

        ActionDocument.Action action = ActionModelConverter.convertToXml(actionModel, parameterModelList);
        rest.setAction(action);

        this.loggingModel.setRestRequest(restDocument);
        RestDocument response = xmppService.sendRestDocument(this.uri, restDocument);
        this.loggingModel.setRestResponse(response);
        ParameterModel result = null;

        if (response.getRest().isSetAction()
                && response.getRest().getAction().isSetResult()) {
            result = ActionModelConverter.convertToModel(response.getRest().getAction().getResult(), actionModel.getResultDocumentation());
        }

        return result;
    }
}
