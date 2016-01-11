package de.tu_berlin.cit.intercloud.client.model.occi.convert;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.ClassificationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class TemplateHelper {
    private static final Logger logger = LoggerFactory.getLogger(TemplateHelper.class);

    public static ClassificationModel addTemplatesToClassificationModel(ClassificationModel classificationModel, MethodDocument.Method method) {
        List<CategoryDocument.Category> templateDocuments = getTemplateDocuments(method);
        for (CategoryDocument.Category template : templateDocuments) {
            addTemplatesToClassificationModel(classificationModel, template);
        }
        return classificationModel;
    }

    private static void addTemplatesToClassificationModel(ClassificationModel classificationModel, CategoryDocument.Category category) {
        if (null != category) {
            addKindTemplates(classificationModel, category.getKind());
            addMixinTemplates(classificationModel, category.getMixinArray());
            addLinkTemplates(classificationModel, category.getLinkArray());
        }
    }

    private static void addKindTemplates(ClassificationModel classificationModel, CategoryType kindType) {
        KindModel kindModel = classificationModel.getKind();
        if (null != kindModel && kindModel.getId().equals(kindType.getSchema() + kindType.getTerm())) {
            if (null != kindType.getTitle()) {
                kindModel.addTemplate(kindType.getTitle());
            } else {
                logger.warn("Could not add Kind Template without a Title. {}, {}", kindModel, kindType);
            }
        } else {
            logger.warn("Could not find Kind Classification for Template. {}, {}", kindModel, kindType);
        }
    }

    private static void addMixinTemplates(ClassificationModel classificationModel, CategoryType[] mixinTypeArray) {
        if (null != mixinTypeArray && 0 < mixinTypeArray.length) {
            for (CategoryType mixinType : mixinTypeArray) {
                MixinModel mixinModel = classificationModel.getMixin(mixinType.getSchema(), mixinType.getTerm());
                if (null != mixinModel) {
                    if (null != mixinType.getTitle()) {
                        mixinModel.addTemplate(mixinType.getTitle());
                    } else {
                        logger.warn("Could not add Mixin Template without a Title. {}, {}", mixinModel, mixinType);
                    }
                } else {
                    logger.warn("Could not find Mixin Classification for Template. {}", mixinType);
                }
            }
        }
    }

    private static void addLinkTemplates(ClassificationModel classificationModel, LinkType[] linkTypeArray) {
        if (null != linkTypeArray && 0 < linkTypeArray.length) {
            for (LinkType linkType : linkTypeArray) {
                LinkModel linkModel = classificationModel.getLink(linkType.getSchema(), linkType.getTerm());
                if (null != linkModel) {
                    if (null != linkType.getTitle()) {
                        linkModel.addTemplate(linkType.getTitle());
                    } else {
                        logger.warn("Could not add Link Template without a Title. {}, {}", linkModel, linkType);
                    }
                    addMixinTemplates(classificationModel, linkType.getMixinArray());
                } else {
                    logger.warn("Could not find Link Classification for Template. {}", linkType);
                }
            }

        }
    }

    private static List<CategoryDocument.Category> getTemplateDocuments(MethodDocument.Method method) {
        List<CategoryDocument.Category> result = new ArrayList<>();
        if (method.isSetRequest()
                && null != method.getRequest().getTemplateArray()
                && 0 < method.getRequest().getTemplateArray().length) {
            for (String template : method.getRequest().getTemplateArray()) {
                try {
                    CategoryDocument templateDocument = CategoryDocument.Factory.parse(template);
                    if (null != templateDocument.getCategory()) {
                        result.add(templateDocument.getCategory());
                    }
                } catch (XmlException e) {
                    logger.error("Failed to parse Template. {}", template, e);
                }
            }
        }
        return result;
    }

    public static CategoryModel applyTemplate(CategoryModel categoryModel, MethodDocument.Method method, String templateTitle) {
        // clear all attributes
        for (AttributeModel a : categoryModel.getAttributes()) {
            a.clearValue();
        }
        if (null == templateTitle) {
            // TODO default values (taken from ClassificationModel)
            categoryModel.setTitle(null); // TODO Title from ClassificationModel
            return categoryModel;
        }
        List<CategoryDocument.Category> templateDocuments = getTemplateDocuments(method);
        if (categoryModel instanceof KindModel) {
            applyKindTemplate((KindModel) categoryModel, templateDocuments, templateTitle);
        } else if (categoryModel instanceof MixinModel) {
            if (null == applyMixinTemplate((MixinModel) categoryModel, templateDocuments, templateTitle)) {
                if (null == applyLinkMixinTemplate((MixinModel) categoryModel, templateDocuments, templateTitle)) {
                    logger.warn("Mixin Template not found. title: {}, {}", templateTitle, categoryModel);
                }
            }
        } else if (categoryModel instanceof LinkModel) {
            // TODO link + mixin template?
            applyLinkTemplate((LinkModel) categoryModel, templateDocuments, templateTitle);
        }
        return categoryModel;
    }


    private static KindModel applyKindTemplate(KindModel kindModel, List<CategoryDocument.Category> templateDocuments, String templateTitle) {
        for (CategoryDocument.Category templateDocument : templateDocuments) {
            CategoryType kindType = templateDocument.getKind();
            if (null != kindType
                    && kindModel.getId().equals(kindType.getSchema() + kindType.getTerm())
                    && templateTitle.equals(kindType.getTitle())) {
                kindModel.setTitle(templateTitle);
                applyAttributes(kindModel, kindType.getAttributeArray());
                return kindModel;
            }
        }
        logger.warn("Kind Template not found. title: {}, {}", templateTitle, kindModel);
        return null;
    }

    private static MixinModel applyMixinTemplate(MixinModel mixinModel, List<CategoryDocument.Category> templateDocuments, String templateTitle) {
        for (CategoryDocument.Category templateDocument : templateDocuments) {
            CategoryType[] mixinTypeArray = templateDocument.getMixinArray();
            if (null != mixinTypeArray) {
                for (CategoryType mixinType : mixinTypeArray) {
                    if (mixinModel.getId().equals(mixinType.getSchema() + mixinType.getTerm())
                            && templateTitle.equals(mixinType.getTitle())) {
                        mixinModel.setTitle(templateTitle);
                        applyAttributes(mixinModel, mixinType.getAttributeArray());
                        return mixinModel;
                    }
                }
            }
        }
        return null;
    }

    private static MixinModel applyLinkMixinTemplate(MixinModel mixinModel, List<CategoryDocument.Category> templateDocuments, String templateTitle) {
        for (CategoryDocument.Category templateDocument : templateDocuments) {
            if (null != templateDocument.getLinkArray() && 0 < templateDocument.getLinkArray().length) {
                for (LinkType linkType : templateDocument.getLinkArray()) {
                    if (null != linkType.getMixinArray() && 0 < linkType.getMixinArray().length) {
                        for (CategoryType mixinType : linkType.getMixinArray()) {
                            if (mixinModel.getId().equals(mixinType.getSchema() + mixinType.getTerm())
                                    && templateTitle.equals(mixinType.getTitle())) {
                                applyAttributes(mixinModel, mixinType.getAttributeArray());
                                return mixinModel;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static LinkModel applyLinkTemplate(LinkModel linkModel, List<CategoryDocument.Category> templateDocuments, String templateTitle) {
        for (CategoryDocument.Category templateDocument : templateDocuments) {
            LinkType[] linkTypeArray = templateDocument.getLinkArray();
            if (null != linkTypeArray) {
                for (LinkType linkType : linkTypeArray) {
                    if (linkModel.getId().equals(linkType.getSchema() + linkType.getTerm())
                            && templateTitle.equals(linkType.getTitle())) {
                        linkModel.setTitle(templateTitle);
                        applyAttributes(linkModel, linkType.getAttributeArray());
                        return linkModel;
                    }
                }
            }
        }
        logger.warn("Link Template not found. title: {}, {}", templateTitle, linkModel);
        return null;
    }

    private static void applyAttributes(CategoryModel categoryModel, AttributeType[] attributeTypes) {
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
                                byte[] signature = Base64.getDecoder().decode(type.getSIGNATURE());
                                model.setSignature(new String(signature));
                                break;
                            case KEY:
                                byte[] key = Base64.getDecoder().decode(type.getKEY());
                                model.setKey(new String(key));
                                break;
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
}
