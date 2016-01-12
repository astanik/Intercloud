package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.exception.AttributeFormatException;
import de.tu_berlin.cit.intercloud.client.exception.MissingClassificationException;
import de.tu_berlin.cit.intercloud.client.exception.ParameterFormatException;
import de.tu_berlin.cit.intercloud.client.exception.UnsupportedMethodException;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.ClassificationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.ClassificationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.RepresentationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.RepresentationModelConverter;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.TemplateHelper;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.convert.ActionModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.rest.action.convert.ActionModelConverter;
import de.tu_berlin.cit.intercloud.client.model.rest.method.AbstractRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.OcciListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.TemplateModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.TextRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.UriListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.UriRepresentationModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciMethodInvocation;
import de.tu_berlin.cit.intercloud.occi.core.OcciListXml;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryListDocument;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.PlainText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
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
import java.util.Arrays;
import java.util.List;

public class IntercloudClient implements IIntercloudClient {
    private static final Logger logger = LoggerFactory.getLogger(IntercloudClient.class);

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
    public AbstractRepresentationModel getRepresentationModel(MethodModel methodModel) throws UnsupportedMethodException, MissingClassificationException {
        AbstractRepresentationModel result = null;
        if (null == methodModel.getRequestMediaType()) {
            // do nothing --> return null
        } else if (PlainText.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            // text/plain
            result = new TextRepresentationModel();
        } else if (UriText.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            // text/uri
            result = new UriRepresentationModel();
        } else if (UriListText.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            // text/uri-list
            result = new UriListRepresentationModel();
        } else if (OcciXml.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            // xml/occi - occi representation model
            result = buildOcciRepresentationModel(methodModel);
        } else if (OcciListXml.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            // xml/occi-list - occi list representation model
            OcciRepresentationModel representationModel = buildOcciRepresentationModel(methodModel);
            OcciListRepresentationModel listRepresentationModel = new OcciListRepresentationModel(Arrays.asList(representationModel));
            result = listRepresentationModel;
        } else {
            throw new UnsupportedMethodException("The request media type is not supported.");
        }

        return result;
    }

    private OcciRepresentationModel buildOcciRepresentationModel(MethodModel methodModel) throws MissingClassificationException {
        long start = System.currentTimeMillis();

        MethodDocument.Method methodDocument = methodModel.getReference();
        ClassificationDocument.Classification classificationDocument = occiClient.getClassification();
        if (null == classificationDocument) {
            throw new MissingClassificationException("Classification is not specified in xwadl.");
        }
        ClassificationModel classificationModel = ClassificationModelBuilder.build(classificationDocument);
        TemplateHelper.addTemplatesToClassificationModel(classificationModel, methodDocument);
        OcciRepresentationModel representation = RepresentationModelBuilder.build(classificationModel);

        logger.info("XmlBean --> RepresentationModel: {} ms", System.currentTimeMillis() - start);
        return representation;
    }

    @Override
    public CategoryModel applyTemplate(CategoryModel categoryModel, TemplateModel templateModel) {
        // apply template
        TemplateHelper.applyTemplate(categoryModel, templateModel);
        return categoryModel;
    }

    @Override
    public AbstractRepresentationModel executeMethod(AbstractRepresentationModel requestRepresentationModel, MethodModel methodModel)
            throws XMPPException, IOException, SmackException, AttributeFormatException, XmlException, UnsupportedMethodException {
        MethodDocument.Method methodDocument = methodModel.getReference();

        // Request: RepresentationModel --> ResourceDocument (rest xml)
        OcciMethodInvocation methodInvocation = occiClient.buildMethodInvocation(methodDocument);
        if (null == requestRepresentationModel && null == methodModel.getRequestMediaType()) {
            // do nothing
        } else if (requestRepresentationModel instanceof TextRepresentationModel && PlainText.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            methodInvocation.setRequestRepresentation(((TextRepresentationModel) requestRepresentationModel).getText());
        } else if (requestRepresentationModel instanceof UriRepresentationModel && UriText.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            methodInvocation.setRequestRepresentation(((UriRepresentationModel) requestRepresentationModel).getUri());
        } else if (requestRepresentationModel instanceof UriListRepresentationModel && UriListText.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            methodInvocation = invokeMethod(methodInvocation, (UriListRepresentationModel) requestRepresentationModel);
        } else if (requestRepresentationModel instanceof OcciRepresentationModel && OcciXml.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            CategoryDocument categoryDocument = RepresentationModelConverter.convertToXml((OcciRepresentationModel) requestRepresentationModel);
            methodInvocation.setRequestRepresentation(categoryDocument.toString());
        } else if (requestRepresentationModel instanceof OcciListRepresentationModel && OcciListXml.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            CategoryListDocument categoryListDocument = RepresentationModelConverter.convertToXml((OcciListRepresentationModel) requestRepresentationModel);
            methodInvocation.setRequestRepresentation(categoryListDocument.toString());
        } else {
            throw new UnsupportedMethodException("Cannot execute method: method not supported. " + methodModel);
        }

        loggingModel.setRestRequest(methodInvocation.getXmlDocument());
        ResourceDocument response = xmppService.sendRestDocument(this.uri, methodInvocation.getXmlDocument());
        loggingModel.setRestResponse(response);
        // Response: ResourceDocument (rest xml) --> RepresentationModel
        AbstractRepresentationModel representationModel = null;
        if (response.getResource().isSetMethod()
                && response.getResource().getMethod().isSetResponse()) {
            String responseMediaType = response.getResource().getMethod().getResponse().getMediaType();
            String responseRepresentation = response.getResource().getMethod().getResponse().getRepresentation();
            if (UriText.MEDIA_TYPE.equals(methodModel.getResponseMediaType())
                    && UriText.MEDIA_TYPE.equals(responseMediaType)) {
                representationModel = new UriRepresentationModel(responseRepresentation);
            } else if (UriListText.MEDIA_TYPE.equals(methodModel.getResponseMediaType())
                    && UriListText.MEDIA_TYPE.equals(responseMediaType)) {
                representationModel = parseUriListMethodResponse(responseRepresentation);
            } else if (OcciXml.MEDIA_TYPE.equals(methodModel.getResponseMediaType())
                    && OcciXml.MEDIA_TYPE.equals(responseMediaType)) {
                representationModel = parseOcciMethodResponse(responseRepresentation);
            } else if (OcciListXml.MEDIA_TYPE.equals(methodModel.getResponseMediaType())
                    && OcciListXml.MEDIA_TYPE.equals(responseMediaType)) {
                representationModel = parseOcciListMethodResponse(responseRepresentation);
            } else {
                representationModel = new TextRepresentationModel(responseRepresentation);
            }
        }

        return representationModel;
    }

    private OcciMethodInvocation invokeMethod(OcciMethodInvocation methodInvocation, UriListRepresentationModel representationModel) {
        List<String> uriList = representationModel.getUriList();
        if (null != uriList) {
            methodInvocation.setRequestRepresentation(String.join(";", uriList));
        }
        return methodInvocation;
    }

    private OcciRepresentationModel parseOcciMethodResponse(String response) throws XmlException {
        CategoryDocument categoryDocument = CategoryDocument.Factory.parse(response);
        ClassificationModel classificationModel = ClassificationModelBuilder.build(occiClient.getClassification());
        return RepresentationModelBuilder.build(classificationModel, categoryDocument);
    }

    private OcciListRepresentationModel parseOcciListMethodResponse(String response) throws XmlException {
        CategoryListDocument categoryListDocument = CategoryListDocument.Factory.parse(response);
        ClassificationModel classificationModel = ClassificationModelBuilder.build(occiClient.getClassification());
        return RepresentationModelBuilder.build(classificationModel, categoryListDocument);
    }

    private UriListRepresentationModel parseUriListMethodResponse(String response) {
        UriListRepresentationModel uriRepresentationModel = new UriListRepresentationModel();
        if (null != response) {
            String[] links = response.split(";");
            if (null != links) {
                uriRepresentationModel.setUriList(Arrays.asList(links));
            }
        }
        return uriRepresentationModel;
    }

    @Override
    public List<ActionModel> getActions() {
        return ActionModelBuilder.buildActionModels(occiClient.getResourceTypeDocument());
    }

    @Override
    public ParameterModel executeAction(ActionModel actionModel) throws ParameterFormatException, XMPPException, IOException, SmackException {
        ResourceDocument resourceDocument = ResourceDocument.Factory.newInstance();
        ResourceDocument.Resource resource = resourceDocument.addNewResource();
        resource.setPath(this.occiClient.getResourceTypeDocument().getResourceType().getPath());

        ActionDocument.Action action = ActionModelConverter.convertToXml(actionModel);
        resource.setAction(action);

        this.loggingModel.setRestRequest(resourceDocument);
        ResourceDocument response = xmppService.sendRestDocument(this.uri, resourceDocument);
        this.loggingModel.setRestResponse(resourceDocument);
        ParameterModel result = null;

        if (response.getResource().isSetAction()
                && response.getResource().getAction().isSetResult()) {
            if (null != actionModel.getResult()) {
                result = ActionModelConverter.convertToModel(response.getResource().getAction().getResult(), actionModel.getResult().getDocumentation());
            } else {
                result = ActionModelConverter.convertToModel(response.getResource().getAction().getResult(), null);
            }
        }

        return result;
    }
}
