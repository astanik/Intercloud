package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.exception.MissingClassificationException;
import de.tu_berlin.cit.intercloud.client.exception.UnsupportedMethodException;
import de.tu_berlin.cit.intercloud.client.model.IMixinModelContainer;
import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
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
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
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
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.PlainText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.apache.commons.lang3.SerializationUtils;
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
        ClassificationDocument.Classification classification = occiClient.getClassification();
        if (null == classification) {
            throw new MissingClassificationException("Classification is not specified in xwadl. ");
        }

        KindModel kindModel = null;
        Map<String, LinkModel> linkMap = new HashMap<>();
        Map<String, MixinModel> mixinMap = new HashMap<>();

        // read kind from classification
        if (null != classification.getKindType()) {
            kindModel = parseKindModel(classification.getKindType());
        }
        // read links from classification
        if (null != classification.getLinkTypeArray() && 0 < classification.getLinkTypeArray().length) {
            for (LinkClassification c : classification.getLinkTypeArray()) {
                LinkModel linkModel = parseLinkModel(c);
                linkMap.put(linkModel.getId(), linkModel);
            }
        }
        // read mixins from classification
        if (null != classification.getMixinTypeArray() && 0 < classification.getMixinTypeArray().length) {
            for (MixinClassification c : classification.getMixinTypeArray()) {
                MixinModel mixinModel = parseMixinModel(c);
                mixinMap.put(mixinModel.getId(), mixinModel);
            }
        }
        // read templates from method document
        addTemplates(methodDocument, kindModel, mixinMap, linkMap);
        // result
        OcciRepresentationModel representation = buildOcciRepresentationModel(kindModel, linkMap, mixinMap);

        logger.info("XmlBean --> RepresentationModel: {} ms", System.currentTimeMillis() - start);
        return representation;
    }

    OcciRepresentationModel buildOcciRepresentationModel(KindModel kindModel,
                                                                 Map<String, LinkModel> linkModelMap,
                                                                 Map<String, MixinModel> mixinModelMap) {
        OcciRepresentationModel representationModel = new OcciRepresentationModel();
        representationModel.setKind(kindModel);
        representationModel.setLinks(linkModelMap.values());

        // list of mixins that apply to other mixins
        List<MixinModel> mixinAppliesMixin = new ArrayList<>();
        // key = mixin.id | value = list of all containers that contain this mixin
        Map<String, List<IMixinModelContainer>> mixinContainersMap = new HashMap<>();

        // apply mixins to representation and links
        // collect mixins that apply to other mixins
        for (MixinModel mixin : mixinModelMap.values()) {
            if (null == mixin.getApplies()) {
                logger.error("Mixin missing applies. {}", mixin);
            } else if ((Category.CategorySchema + Category.CategoryTerm).equals(mixin.getApplies())) {
                // default
                List<IMixinModelContainer> mixinContainers = new ArrayList<>();
                // apply mixin to representation
                representationModel.addMixin(mixin);
                mixinContainers.add(representationModel);
                // clone mixin to all links
                for (LinkModel link : linkModelMap.values()) {
                    MixinModel clone = SerializationUtils.clone(mixin);
                    link.addMixin(clone);
                    mixinContainers.add(link);
                }
                mixinContainersMap.put(mixin.getId(), mixinContainers);
            } else if (mixinModelMap.containsKey(mixin.getApplies())) {
                // applies to Mixin?
                mixinAppliesMixin.add(mixin);
            } else if (null != kindModel && kindModel.getId().equals(mixin.getApplies())) {
                // applies to Kind?
                representationModel.addMixin(mixin);
                mixinContainersMap.put(mixin.getId(), Arrays.asList(representationModel));
            } else if (linkModelMap.containsKey(mixin.getApplies())) {
                // applies to Link?
                LinkModel link = linkModelMap.get(mixin.getApplies());
                link.addMixin(mixin);
                mixinContainersMap.put(mixin.getId(), Arrays.asList(link));
            } else {
                // applies to none
                logger.error("Mixin does not apply to a Category. {}", mixin);
            }
        }

        // mixinAppliesMixin stuff
        int size = 0;
        while (size != mixinAppliesMixin.size()) {
            size = mixinAppliesMixin.size();
            for (int i = 0; i < mixinAppliesMixin.size(); ) {
                MixinModel mixin = mixinAppliesMixin.get(i);
                List<IMixinModelContainer> mixinContainers = mixinContainersMap.get(mixin.getApplies());
                if (null != mixinContainers && !mixinContainers.isEmpty()) {
                    mixinAppliesMixin.remove(i);
                    // add container
                    mixinContainersMap.put(mixin.getId(), mixinContainers);
                    // apply first
                    mixinContainers.get(0).addMixin(mixin);
                    // clone others
                    for (int k = 1; k < mixinContainers.size(); k++) {
                        MixinModel clone = SerializationUtils.clone(mixin);
                        mixinContainers.get(k).addMixin(clone);
                    }
                } else {
                    i++;
                }
            }
        }
        if (!mixinAppliesMixin.isEmpty()) {
            logger.error("Some mixins could not be applied. {}", mixinAppliesMixin);
        }

        return representationModel;
    }

    private KindModel parseKindModel(CategoryClassification classification) {
        KindModel model = new KindModel(classification.getTerm(), classification.getSchema());
        addAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private MixinModel parseMixinModel(MixinClassification classification) {
        MixinModel model = new MixinModel(classification.getTerm(), classification.getSchema(), classification.getApplies());
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
    public AbstractRepresentationModel executeMethod(AbstractRepresentationModel requestRepresentationModel, MethodModel methodModel) throws XMPPException, IOException, SmackException {
        MethodDocument.Method methodDocument = getMethodDocument(methodModel);
        if (null == methodDocument) {
            throw new IllegalArgumentException("Cannot execute Request: method not supported by the resource. " + methodModel);
        }
        AbstractRepresentationModel representationModel = null;
        if (null == requestRepresentationModel && null == methodModel.getRequestMediaType()) {
            OcciMethodInvocation methodInvocation = occiClient.buildMethodInvocation(methodDocument);
            ResourceDocument response = xmppService.sendRestDocument(this.uri, methodInvocation.getXmlDocument());
            if (UriText.MEDIA_TYPE.equals(methodModel.getResponseMediaType()) || UriListText.MEDIA_TYPE.equals(methodModel.getResponseMediaType())) {
                // URI
                UriListRepresentationModel uriRepresentationModel = new UriListRepresentationModel();
                representationModel = uriRepresentationModel;
                String s = response.getResource().getMethod().getResponse().getRepresentation();
                if (null != s) {
                    String[] links = s.split(";");
                    if (null != links) {
                        uriRepresentationModel.setUriList(Arrays.asList(links));
                    }
                }
            } else {
                representationModel = new TextRepresentationModel(response.toString());
            }
        }

        return representationModel;
    }

    @Override
    public String toString() {
        return occiClient.getResourceTypeDocument().toString();
    }
}
