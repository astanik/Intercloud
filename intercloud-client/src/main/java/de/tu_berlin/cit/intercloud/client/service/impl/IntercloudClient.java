package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.RequestModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciMethodInvocation;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.LinkClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.MixinClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
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
                result.add(new MethodModel(m.getType().toString(), requestMediaType, responseMediaType, documentation));
            }
        }
        return result;
    }

    @Override
    public RequestModel getRequestModel(MethodModel methodModel) {
        RequestModel requestModel = null;
        // occi/xml
        if (OcciXml.MEDIA_TYPE.equals(methodModel.getRequestMediaType())) {
            MethodDocument.Method methodDocument = getMethodDocument(methodModel);
            if (null != methodDocument) {
                Map<String, CategoryModel> categoryModelMap = new HashMap<>();
                ClassificationDocument.Classification classification = occiClient.getClassification();
                // read kind from classification
                if (null != classification.getKindType()) {
                    KindModel kindModel = parseKindModel(classification.getKindType());
                    categoryModelMap.put(kindModel.getSchema() + kindModel.getTerm(), kindModel);
                }
                // read links from classification
                if (null != classification.getLinkTypeArray() && 0 < classification.getLinkTypeArray().length) {
                    for (LinkClassification c : classification.getLinkTypeArray()) {
                        LinkModel linkModel = parseLinkModel(c);
                        categoryModelMap.put(linkModel.getSchema() + linkModel.getTerm(), linkModel);
                    }
                }
                // read mixins from classification
                if (null != classification.getMixinTypeArray() && 0 < classification.getMixinTypeArray().length) {
                    for (MixinClassification c : classification.getMixinTypeArray()) {
                        MixinModel mixinModel = parseMixinModel(c);
                        categoryModelMap.put(mixinModel.getSchema() + mixinModel.getTerm(), mixinModel);
                    }
                }
                // read templates from method document
                addTemplates(categoryModelMap, methodDocument);
                if (categoryModelMap!=null);
            } else {
                throw new IllegalArgumentException("Cannot create Request model: method does not exist. " + methodModel);
            }
        } else {
            throw new UnsupportedOperationException("Cannot create Request model: media type is not supported. " + methodModel);
        }
        return requestModel;
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

    private void addTemplates(Map<String, CategoryModel> categoryModelMap, MethodDocument.Method methodDocument) {
        if (methodDocument.isSetRequest()) {
            if (null != methodDocument.getRequest().getTemplateArray() && 0 < methodDocument.getRequest().getTemplateArray().length) {
                for (String t : methodDocument.getRequest().getTemplateArray()) {
                    try {
                        CategoryDocument categoryDocument = CategoryDocument.Factory.parse(t);
                        addTemplates(categoryModelMap, categoryDocument.getCategory());
                    } catch (XmlException e) {
                        logger.warn("Failed to parse Category Document. {}", t, e);
                    }
                }
            }
        }
    }

    private void addTemplates(Map<String, CategoryModel> categoryModelMap, CategoryDocument.Category categoryDocument) {
        // TODO cache templates --> use cached templates in applyTemplate
        if (null != categoryDocument.getKind()) {
            CategoryType kind = categoryDocument.getKind();
            CategoryModel categoryModel = categoryModelMap.get(kind.getSchema() + kind.getTerm());
            if (null != categoryModel && categoryModel instanceof KindModel) {
                categoryModel.addTemplate(kind.getTitle());
            } else {
                logger.warn("Could not find Classification for Template: " + kind);
            }
        }

        if (null != categoryDocument.getMixinArray() && 0 < categoryDocument.getMixinArray().length) {
            for (CategoryType mixin : categoryDocument.getMixinArray()) {
                CategoryModel categoryModel = categoryModelMap.get(mixin.getSchema() + mixin.getTerm());
                if (null != categoryModel && categoryModel instanceof MixinModel) {
                    categoryModel.addTemplate(mixin.getTitle());
                } else {
                    logger.warn("Could not find Classification for Template: " + mixin);
                }
            }
        }
    }

    private MethodDocument.Method getMethodDocument(MethodModel methodModel) {
        return occiClient.getMethod(MethodType.Enum.forString(methodModel.getMethodType()), methodModel.getRequestMediaType(), methodModel.getResponseMediaType());
    }

    @Override
    public CategoryModel applyTemplate(CategoryModel categoryModel, MethodModel methodModel, String Template) {
        return categoryModel;
    }

    @Override
    public String executeRequest(RequestModel requestModel, MethodModel methodModel) throws XMPPException, IOException, SmackException {
        MethodDocument.Method methodDocument = getMethodDocument(methodModel);
        if (null == methodDocument) {
            throw new IllegalArgumentException("Cannot execute Request: method not supported by the resource. " + methodModel);
        }
        if (null == requestModel && !methodModel.hasRequest()) {
            OcciMethodInvocation methodInvocation = occiClient.buildMethodInvocation(methodDocument);
            ResourceDocument response = xmppService.sendRestDocument(this.uri, methodInvocation.getXmlDocument());
            return response.toString();
        }

        throw new UnsupportedOperationException("Cannot execute Request: method not supported. " + methodModel);
    }

    @Override
    public String toString() {
        return occiClient.getResourceTypeDocument().toString();
    }
}
