package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.AbstractRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.RequestModel;
import de.tu_berlin.cit.intercloud.client.model.rest.TextRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.UriRepresentationModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciMethodInvocation;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.LinkClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.MixinClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntercloudClient implements IIntercloudClient {
    private static final Logger logger = LoggerFactory.getLogger(IntercloudClient.class);

    private final IXmppService xmppService;
    private final OcciClient occiClient;
    private final XmppURI uri;

    public IntercloudClient(IXmppService xmppService, ResourceTypeDocument xwadl, XmppURI uri) {
        this.xmppService = xmppService;
        this.occiClient = new OcciClient(xwadl);
        this.uri = uri;
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

/*
    TODO: create representation
    Kind
    List<Mixin>
    List<Link> --> List<Mixin>
*/

    private Object getAny(Map map, List<String> keys) {
        for (String key : keys) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }
    private OcciRepresentationModel createRepresentation(KindModel kindModel,
                                                         Map<String, LinkModel> linkModelMap,
                                                         Map<String, MixinModel> mixinModelMap) {
        for (MixinModel mixin : mixinModelMap.values()) {
            // default
            if (mixin.getApplies().contains("http://schema.ogf.org/occi/core#category")) {

            }
            // applies to mixin?
            if (kindModel != null && mixin.getApplies().contains(kindModel.getSchema() + kindModel.getTerm())) {

            }
            // applies to link?

            // applies to mixin?
        }

        return null;
    }

    @Override
    public RequestModel getRequestModel(MethodModel methodModel) {
        if (!methodModel.hasRequest()) {
            return null;
        } else if (OcciXml.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            // occi/xml
            MethodDocument.Method methodDocument = getMethodDocument(methodModel);
            if (null != methodDocument) {
                KindModel kindModel = null;
                Map<String, LinkModel> linkMap = new HashMap<>();
                Map<String, MixinModel> mixinMap = new HashMap<>();

                ClassificationDocument.Classification classification = occiClient.getClassification();
                // read kind from classification
                if (null != classification.getKindType()) {
                    kindModel = parseKindModel(classification.getKindType());
                }
                // read links from classification
                if (null != classification.getLinkTypeArray() && 0 < classification.getLinkTypeArray().length) {
                    for (LinkClassification c : classification.getLinkTypeArray()) {
                        LinkModel linkModel = parseLinkModel(c);
                        linkMap.put(linkModel.getSchema() + linkModel.getTerm(), linkModel);
                    }
                }
                // read mixins from classification
                if (null != classification.getMixinTypeArray() && 0 < classification.getMixinTypeArray().length) {
                    for (MixinClassification c : classification.getMixinTypeArray()) {
                        MixinModel mixinModel = parseMixinModel(c);
                        mixinMap.put(mixinModel.getSchema() + mixinModel.getTerm(), mixinModel);
                    }
                }
                // read templates from method document
                addTemplates(methodDocument, kindModel, mixinMap, linkMap);
                // TODO: create representation structure
                // result
                RequestModel requestModel = new RequestModel();
                requestModel.setKindModel(kindModel);
                requestModel.setMixinModels(new ArrayList<>(mixinMap.values()));
                return requestModel;
            } else {
                throw new IllegalArgumentException("Failed to create Request model: method does not exist. " + methodModel);
            }
        } else {
            throw new UnsupportedOperationException("Cannot create Request model: media type is not supported. " + methodModel);
        }
    }

    private KindModel parseKindModel(CategoryClassification classification) {
        KindModel model = new KindModel(classification.getTerm(), classification.getSchema());
        addAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private MixinModel parseMixinModel(MixinClassification classification) {
        MixinModel model = new MixinModel(classification.getTerm(), classification.getSchema(), classification.getAppliesArray());
        addAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private LinkModel parseLinkModel(LinkClassification classification) {
        LinkModel model = new LinkModel(classification.getTerm(), classification.getSchema(), classification.getRelation());
        addAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private CategoryModel addAttributeModels(CategoryModel categoryModel, AttributeClassificationDocument.AttributeClassification[] attributeClassifications) {
        if (null != attributeClassifications && 0 < attributeClassifications.length) {
            for (AttributeClassificationDocument.AttributeClassification a : attributeClassifications) {
                categoryModel.addAttribute(new AttributeModel(a.getName(), a.getType().toString(), a.getRequired(), a.getMutable(), a.getDescription()));
            }
        }
        return categoryModel;
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

    private void addTemplates(MethodDocument.Method methodDocument, KindModel kindModel, Map<String, MixinModel> mixinMap, Map<String, LinkModel> linkMap) {
        List<CategoryDocument> templateDocuments = getTemplateDocuments(methodDocument);
        for (CategoryDocument template : templateDocuments) {
            addTemplates(template.getCategory(), kindModel, mixinMap, linkMap);
        }
    }

    private void addTemplates(CategoryDocument.Category categoryDocument, KindModel kindModel, Map<String, MixinModel> mixinMap, Map<String, LinkModel> linkMap) {
        if (null != categoryDocument.getKind()) {
            CategoryType kindType = categoryDocument.getKind();
            if (kindModel.getSchema().equals(kindType.getSchema()) && kindModel.getTerm().equals(kindType.getTerm())) {
                kindModel.addTemplate(kindType.getTitle());
            } else {
                logger.warn("Could not find Kind Classification for Template: " + kindModel);
            }
        }

        if (null != categoryDocument.getMixinArray() && 0 < categoryDocument.getMixinArray().length) {
            for (CategoryType mixinType : categoryDocument.getMixinArray()) {
                MixinModel mixinModel = mixinMap.get(mixinType.getSchema() + mixinType.getTerm());
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
    public CategoryModel applyTemplate(CategoryModel categoryModel, MethodModel methodModel, String templateTitle) {
        if (null == categoryModel || null == methodModel) {
            return null;
        }
        if (null == templateTitle) {
            categoryModel.setTitle(null);
            // clear all attributes
            for (AttributeModel a : categoryModel.getAttributes()) {
                a.clearValue();
            }
            return categoryModel;
        }
        // apply template
        MethodDocument.Method methodDocument = getMethodDocument(methodModel);
        if (null == methodDocument) {
            throw new IllegalArgumentException("Failed to apply template: method does not exist. " + methodModel);
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
                                model.setDatetime(new Date(type.getDATETIME().getTimeInMillis()));
                                break;
                            case SIGNATURE:
                            case KEY:
                            case DURATION:
                            default:
                                logger.info("Cannot set attribute, type is not supported. model: {}, type: {}", model, type);
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
    public AbstractRepresentationModel executeRequest(RequestModel requestModel, MethodModel methodModel) throws XMPPException, IOException, SmackException {
        MethodDocument.Method methodDocument = getMethodDocument(methodModel);
        if (null == methodDocument) {
            throw new IllegalArgumentException("Cannot execute Request: method not supported by the resource. " + methodModel);
        }
        AbstractRepresentationModel representationModel = null;
        if (null == requestModel && !methodModel.hasRequest()) {
            OcciMethodInvocation methodInvocation = occiClient.buildMethodInvocation(methodDocument);
            ResourceDocument response = xmppService.sendRestDocument(this.uri, methodInvocation.getXmlDocument());
            if (UriText.MEDIA_TYPE.equals(methodModel.getResponseMediaType()) || UriListText.MEDIA_TYPE.equals(methodModel.getResponseMediaType())) {
                // URI
                UriRepresentationModel uriRepresentationModel = new UriRepresentationModel();
                representationModel = uriRepresentationModel;
                String s = response.getResource().getMethod().getResponse().getRepresentation();
                if (null != s) {
                    String[] links = s.split(";");
                    if (null != links) {
                        uriRepresentationModel.getUriList().addAll(Arrays.asList(links));
                    }
                }
            } else {
                representationModel = new TextRepresentationModel(response.toString());
            }
        }

        return representationModel;
        //throw new UnsupportedOperationException("Cannot execute Request: method not supported. " + methodModel);
    }

    @Override
    public String toString() {
        return occiClient.getResourceTypeDocument().toString();
    }
}
