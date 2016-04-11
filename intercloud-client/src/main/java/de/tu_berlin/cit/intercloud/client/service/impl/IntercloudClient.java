package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.exception.AttributeFormatException;
import de.tu_berlin.cit.intercloud.client.exception.MissingClassificationException;
import de.tu_berlin.cit.intercloud.client.exception.ParameterFormatException;
import de.tu_berlin.cit.intercloud.client.exception.UnsupportedMethodException;
import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.convert.ActionModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.rest.action.convert.ActionModelConverter;
import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.profiling.IProfilingTask;
import de.tu_berlin.cit.intercloud.client.profiling.ProfilingService;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciMethodInvocation;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ActionDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
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

    public IntercloudClient(IXmppService xmppService, ResourceTypeDocument xwadl, XmppURI uri) {
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
        MethodDocument.Method[] methodArray = occiClient.getResourceTypeDocument().getResourceType().getMethodArray();
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

        return ProfilingService.getInstance().invokeAndProfile(
                new IProfilingTask<IRepresentationModel>() {
                    @Override
                    public String getIdentifier() {
                        return "getRequestModel";
                    }

                    @Override
                    public IRepresentationModel invoke() {
                        return modelPlugin.getRequestModel(methodModel.getReference().getRequest(),
                                occiClient.getResourceTypeDocument().getResourceType().getGrammars());
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
                String representationString = ProfilingService.getInstance().invokeAndProfile(new IProfilingTask<String>() {
                    @Override
                    public String getIdentifier() {
                        return null;
                    }

                    @Override
                    public String invoke() {
                        return modelPlugin.getRepresentationString(requestRepresentationModel);
                    }
                });
                methodInvocation.setRequestRepresentation(representationString);
            } else {
                throw new UnsupportedMethodException("Cannot execute method: method not supported. " + methodModel);
            }
        }

        loggingModel.setRestRequest(methodInvocation.getXmlDocument());
        ResourceDocument response = xmppService.sendRestDocument(this.uri, methodInvocation.getXmlDocument());
        loggingModel.setRestResponse(response);
        // Response: ResourceDocument (rest xml) --> RepresentationModel
        IRepresentationModel representationModel = null;
        if (response.getResource().isSetMethod()
                && response.getResource().getMethod().isSetResponse()) {
            String responseMediaType = response.getResource().getMethod().getResponse().getMediaType();
            IRepresentationModelPlugin modelPlugin = representationModelRegistry.getPlugin(responseMediaType);
            if (null != modelPlugin) {
                representationModel = ProfilingService.getInstance().invokeAndProfile(new IProfilingTask<IRepresentationModel>() {
                    @Override
                    public String getIdentifier() {
                        return "getResponseModel";
                    }

                    @Override
                    public IRepresentationModel invoke() {
                        return modelPlugin.getResponseModel(response.getResource().getMethod().getResponse(),
                                occiClient.getResourceTypeDocument().getResourceType().getGrammars());
                    }
                });
            }
        }

        return representationModel;
    }

    @Override
    public List<ActionModel> getActions() {
        return ActionModelBuilder.buildActionModels(occiClient.getResourceTypeDocument());
    }

    @Override
    public List<ParameterModel> getParameters(ActionModel actionModel) {
        return ActionModelBuilder.buildParameterModelList(actionModel.getReference());
    }

    @Override
    public ParameterModel executeAction(ActionModel actionModel, List<ParameterModel> parameterModelList) throws ParameterFormatException, XMPPException, IOException, SmackException {
        ResourceDocument resourceDocument = ResourceDocument.Factory.newInstance();
        ResourceDocument.Resource resource = resourceDocument.addNewResource();
        resource.setPath(this.uri.getPath());

        ActionDocument.Action action = ActionModelConverter.convertToXml(actionModel, parameterModelList);
        resource.setAction(action);

        this.loggingModel.setRestRequest(resourceDocument);
        ResourceDocument response = xmppService.sendRestDocument(this.uri, resourceDocument);
        this.loggingModel.setRestResponse(resourceDocument);
        ParameterModel result = null;

        if (response.getResource().isSetAction()
                && response.getResource().getAction().isSetResult()) {
            result = ActionModelConverter.convertToModel(response.getResource().getAction().getResult(), actionModel.getResultDocumentation());
        }

        return result;
    }
}
