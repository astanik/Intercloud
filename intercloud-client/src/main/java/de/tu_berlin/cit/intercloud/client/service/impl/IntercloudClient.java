package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.convert.ClassificationModelBuilder;
import de.tu_berlin.cit.intercloud.client.convert.RepresenationModelConverter;
import de.tu_berlin.cit.intercloud.client.convert.RepresentationModelBuilder;
import de.tu_berlin.cit.intercloud.client.exception.AttributeFormatException;
import de.tu_berlin.cit.intercloud.client.exception.MissingClassificationException;
import de.tu_berlin.cit.intercloud.client.exception.UnsupportedMethodException;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.ClassificationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.AbstractRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.OcciListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.TextRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.UriListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.UriRepresentationModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciMethodInvocation;
import de.tu_berlin.cit.intercloud.occi.core.OcciListXml;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.PlainText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
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
        this.loggingModel.setXwadlDocument(xwadl);
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
                String requestMediaType = null;
                String responseMediaType = null;
                String documentation = null;
                if (m.isSetRequest()) {
                    requestMediaType = m.getRequest().getMediaType();
                }
                if (m.isSetResponse()) {
                    responseMediaType = m.getResponse().getMediaType();
                }
                if (null != m.getDocumentation()) {
                    documentation = m.getDocumentation().getStringValue();
                }
                result.add(new MethodModel(this.uri, m.getType().toString(), requestMediaType, responseMediaType, documentation));
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
            OcciListRepresentationModel listRepresentationModel = new OcciListRepresentationModel();
            listRepresentationModel.setOcciRepresentationModels(Arrays.asList(representationModel));
            result = listRepresentationModel;
        } else {
            throw new UnsupportedMethodException("The request media type is not supported.");
        }

        return result;
    }

    private OcciRepresentationModel buildOcciRepresentationModel(MethodModel methodModel) throws MissingClassificationException, UnsupportedMethodException {
        long start = System.currentTimeMillis();

        MethodDocument.Method methodDocument = getMethodDocument(methodModel);
        if (null == methodDocument) {
            throw new UnsupportedMethodException("Method is not specified in xwadl.");
        }
        ClassificationDocument.Classification classificationDocument = occiClient.getClassification();
        if (null == classificationDocument) {
            throw new MissingClassificationException("Classification is not specified in xwadl.");
        }
        ClassificationModel classificationModel = ClassificationModelBuilder.build(classificationDocument);
        // read templates from method document
        addTemplates(methodDocument, classificationModel);
        OcciRepresentationModel representation = RepresentationModelBuilder.build(classificationModel);

        logger.info("XmlBean --> RepresentationModel: {} ms", System.currentTimeMillis() - start);
        return representation;
    }

    private List<CategoryDocument> getTemplateDocuments(MethodDocument.Method methodDocument) {
        List<CategoryDocument> result = new ArrayList<>();
        if (methodDocument.isSetRequest()
                && null != methodDocument.getRequest().getTemplateArray()
                && 0 < methodDocument.getRequest().getTemplateArray().length) {
            for (String template : methodDocument.getRequest().getTemplateArray()) {
                try {
                    CategoryDocument templateDocument = CategoryDocument.Factory.parse(template);
                    result.add(templateDocument);
                } catch (XmlException e) {
                    logger.warn("Failed to parse Template. {}", template, e);
                }
            }
        }
        return result;
    }

    private void addTemplates(MethodDocument.Method methodDocument, ClassificationModel classificationModel) {
        List<CategoryDocument> templateDocuments = getTemplateDocuments(methodDocument);
        for (CategoryDocument template : templateDocuments) {
            addTemplates(template.getCategory(), classificationModel);
        }
    }

    private void addTemplates(CategoryDocument.Category categoryDocument, ClassificationModel classificationModel) {
        if (null != categoryDocument.getKind()) {
            CategoryType kindType = categoryDocument.getKind();
            KindModel kindModel = classificationModel.getKind();
            if (null != kindModel && kindModel.getId().equals(kindType.getSchema() + kindType.getTerm())) {
                kindModel.addTemplate(kindType.getTitle());
            } else {
                logger.warn("Could not find Kind Classification for Template: " + kindModel);
            }
        }

        if (null != categoryDocument.getMixinArray() && 0 < categoryDocument.getMixinArray().length) {
            for (CategoryType mixinType : categoryDocument.getMixinArray()) {
                MixinModel mixinModel = classificationModel.getMixin(mixinType.getSchema() + mixinType.getTerm());
                if (null != mixinModel) {
                    mixinModel.addTemplate(mixinType.getTitle());
                } else {
                    logger.warn("Could not find Mixin Classification for Template: " + mixinType);
                }
            }
        }

        // TODO Link
    }

    private MethodDocument.Method getMethodDocument(MethodModel methodModel) {
        return occiClient.getMethod(MethodType.Enum.forString(methodModel.getMethodType()), methodModel.getRequestMediaType(), methodModel.getResponseMediaType());
    }

    @Override
    public CategoryModel applyTemplate(CategoryModel categoryModel, MethodModel methodModel, String templateTitle) throws UnsupportedMethodException {
        if (null == categoryModel || null == methodModel) {
            return null;
        }
        // clear all attributes
        for (AttributeModel a : categoryModel.getAttributes()) {
            // TODO default values
            a.clearValue();
        }
        if (null == templateTitle) {
            categoryModel.setTitle(null);
            return categoryModel;
        }
        // apply template
        MethodDocument.Method methodDocument = getMethodDocument(methodModel);
        if (null == methodDocument) {
            throw new UnsupportedMethodException("Failed to apply template: method does not exist. " + methodModel);
        }
        List<CategoryDocument> templateDocuments = getTemplateDocuments(methodDocument);
        if (categoryModel instanceof KindModel) {
            applyKindTemplate((KindModel) categoryModel, templateDocuments, templateTitle);
        } else if (categoryModel instanceof MixinModel) {
            applyMixinTemplate((MixinModel) categoryModel, templateDocuments, templateTitle);
            // TODO mixins in link?
        } // TODO Link
        return categoryModel;
    }

    private void applyKindTemplate(KindModel model, List<CategoryDocument> templateDocuments, String templateTitle) {
        for (CategoryDocument templateDocument : templateDocuments) {
            CategoryType type = templateDocument.getCategory().getKind();
            if (null != type
                    && model.getSchema().equals(type.getSchema())
                    && model.getTerm().equals(type.getTerm())
                    && templateTitle.equals(type.getTitle())) {
                model.setTitle(templateTitle);
                applyAttributes(model, type.getAttributeArray());
                return;
            }
        }
        logger.warn("Kind Template not found. title: {}, {}", templateTitle, model);
    }

    private void applyMixinTemplate(MixinModel model, List<CategoryDocument> templateDocuments, String templateTitle) {
        for (CategoryDocument templateDocument : templateDocuments) {
            CategoryType[] mixinArray = templateDocument.getCategory().getMixinArray();
            if (null != mixinArray) {
                for (CategoryType type : mixinArray) {
                    if (model.getSchema().equals(type.getSchema())
                            && model.getTerm().equals(type.getTerm())
                            && templateTitle.equals(type.getTitle())) {
                        model.setTitle(templateTitle);
                        applyAttributes(model, type.getAttributeArray());
                        return;
                    }
                }
            }
        }
        logger.warn("Mixin Template not found. title: {}, {}", templateDocuments, model);
    }

    private void applyAttributes(CategoryModel categoryModel, AttributeType[] attributeTypes) {
        if (attributeTypes != null) {
            for (AttributeType type : attributeTypes) {
                AttributeModel model = categoryModel.getAttribute(type.getName());
                if (null != model) {
                    try {
                        switch (model.getType()) {
                            case STRING:
                                model.setString(type.getSTRING());
                                break;
                            case ENUM:
                                model.setEnum(type.getENUM());
                                break;
                            case INTEGER:
                                model.setInteger(type.getINTEGER());
                                break;
                            case DOUBLE:
                                model.setDouble(type.getDOUBLE());
                                break;
                            case FLOAT:
                                model.setFloat(type.getFLOAT());
                                break;
                            case BOOLEAN:
                                model.setBoolean(type.getBOOLEAN());
                                break;
                            case URI:
                                model.setUri(type.getURI());
                                break;
                            case DATETIME:
                                model.setDatetime(type.getDATETIME().getTime());
                                break;
                            case DURATION:
                                model.setDuration(Duration.parse(type.getDURATION().toString()));
                                break;
                            case LIST:
                                model.setList(Arrays.asList(type.getLIST().getItemArray()));
                                break;
                            case MAP:
                                model.setMap(RepresentationModelBuilder.mapTypeToMap(type.getMAP()));
                                break;
                            case SIGNATURE:
                            case KEY:
                            default:
                                logger.warn("Cannot set attribute, type is not supported. model: {}, type: {}", model, type);
                                break;
                        }
                    } catch (Exception e) {
                        logger.error("Could not set attribute. model: {}, type: {}", model, type, e);
                    }
                } else {
                    logger.warn("Could not find template attribute in classification. type: {}", type);
                }
            }
        }
    }

    @Override
    public AbstractRepresentationModel executeMethod(AbstractRepresentationModel requestRepresentationModel, MethodModel methodModel)
            throws XMPPException, IOException, SmackException, UnsupportedMethodException, AttributeFormatException, XmlException {
        MethodDocument.Method methodDocument = getMethodDocument(methodModel);
        if (null == methodDocument) {
            throw new UnsupportedMethodException("Cannot execute method: method not found. " + methodModel);
        }

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
            methodInvocation = invokeMethod(methodInvocation, (OcciRepresentationModel) requestRepresentationModel);
        } else {
            throw new UnsupportedMethodException("Cannot execute method: method not supported. " + methodModel);
        }

        ResourceDocument response = xmppService.sendRestDocument(this.uri, methodInvocation.getXmlDocument());
        loggingModel.setRestDocument(response);
        // Response: ResourceDocument (rest xml) --> RepresentationModel
        AbstractRepresentationModel representationModel = null;
        if (response.getResource().getMethod().isSetResponse()) {
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

    private OcciMethodInvocation invokeMethod(OcciMethodInvocation methodInvocation, OcciRepresentationModel representationModel) throws AttributeFormatException {
        CategoryDocument categoryDocument = RepresenationModelConverter.convertToXml(representationModel);
        methodInvocation.setRequestRepresentation(categoryDocument.toString());
        return methodInvocation;
    }

    private OcciRepresentationModel parseOcciMethodResponse(String response) throws XmlException {
        CategoryDocument categoryDocument = CategoryDocument.Factory.parse(response);
        ClassificationModel classificationModel = ClassificationModelBuilder.build(occiClient.getClassification());
        return RepresentationModelBuilder.build(classificationModel, categoryDocument);
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
}
