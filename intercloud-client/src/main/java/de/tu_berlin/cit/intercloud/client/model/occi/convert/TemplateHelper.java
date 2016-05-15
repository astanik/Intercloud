package de.tu_berlin.cit.intercloud.client.model.occi.convert;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.ClassificationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.TemplateModel;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.LinkClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.MixinClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.rwx4j.xwadl.RequestDocument;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * This class provides methods to add {@link TemplateModel}s
 * to {@link CategoryModel} sub-types contained in a {@link ClassificationModel}.
 * Further, it provides methods to apply Attribute Values of a Template to the corresponding
 * {@link AttributeModel}.
 */
public class TemplateHelper {
    private static final Logger logger = LoggerFactory.getLogger(TemplateHelper.class);

    /**
     * Add {@link TemplateModel}s to the {@link CategoryModel} sub-types contained in a
     * {@link ClassificationModel}.
     * @param classificationModel The Category Model containing Category Models where to add the Templates to.
     * @param request The XWADL Request containing Templates
     * @return
     */
    public static ClassificationModel addTemplatesToClassificationModel(ClassificationModel classificationModel, RequestDocument.Request request) {
        List<CategoryDocument.Category> templateDocuments = getTemplateDocuments(request);
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
        if (null != kindType) {
            KindModel kindModel = classificationModel.getKind();
            if (null != kindModel && kindModel.getId().equals(kindType.getSchema() + kindType.getTerm())) {
                if (null != kindType.getTitle()) {
                    kindModel.addTemplate(new TemplateModel(kindType.getTitle(), kindType));
                } else {
                    logger.warn("Could not add Kind Template without a Title. {}, {}", kindModel, kindType);
                }
            } else {
                logger.warn("Could not find Kind Classification for Template. {}, {}", kindModel, kindType);
            }
        }
    }

    private static void addMixinTemplates(ClassificationModel classificationModel, CategoryType[] mixinTypeArray) {
        if (null != mixinTypeArray && 0 < mixinTypeArray.length) {
            for (CategoryType mixinType : mixinTypeArray) {
                MixinModel mixinModel = classificationModel.getMixin(mixinType.getSchema(), mixinType.getTerm());
                if (null != mixinModel) {
                    if (null != mixinType.getTitle()) {
                        mixinModel.addTemplate(new TemplateModel(mixinType.getTitle(), mixinType));
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
                        linkModel.addTemplate(new TemplateModel(linkType.getTitle(), linkType));
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

    private static List<CategoryDocument.Category> getTemplateDocuments(RequestDocument.Request request) {
        List<CategoryDocument.Category> result = new ArrayList<>();
        if (null != request && null != request.getTemplateArray()
                && 0 < request.getTemplateArray().length) {
            for (String template : request.getTemplateArray()) {
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

    /**
     * Applies the Attribute Values of a Template to the corresponding
     * {@link AttributeModel}s contained in a {@link CategoryModel}.
     * Further it sets the title as defined by the Template.
     * @param categoryModel The Category Model where to apply the Template to.
     * @param template The Template to be applied.
     * @return
     */
    public static CategoryModel applyTemplate(CategoryModel categoryModel, TemplateModel template) {
        // clear all attributes
        categoryModel.getAttributes().forEach(AttributeModel::clearValue);
        if (null == template) {
            categoryModel.setTitle(null);
            return categoryModel;
        } else if (null == template.getReference()) {
            categoryModel.setTitle(template.getName());
            return categoryModel;
        }
        if (categoryModel instanceof KindModel) {
            if (template.getReference() instanceof CategoryType) {
                // Rest Representation
                applyCategoryTemplate(categoryModel, (CategoryType) template.getReference());
            } else if (template.getReference() instanceof CategoryClassification) {
                // XWADL Classification (default values)
                ClassificationModelBuilder.setAttributeDefaultValues(categoryModel,
                        ((CategoryClassification) template.getReference()).getAttributeClassificationArray());
            }
        } else if (categoryModel instanceof MixinModel) {
            if (template.getReference() instanceof CategoryType) {
                // Rest Representation
                applyCategoryTemplate(categoryModel, (CategoryType) template.getReference());
            } else if (template.getReference() instanceof MixinClassification) {
                // XWADL Classification (default values)
                ClassificationModelBuilder.setAttributeDefaultValues(categoryModel,
                        ((CategoryClassification) template.getReference()).getAttributeClassificationArray());
            }
        } else if (categoryModel instanceof LinkModel) {
            if (template.getReference() instanceof LinkType) {
                // Rest Representation
                applyLinkTemplate((LinkModel) categoryModel, (LinkType) template.getReference());
            } else if (template.getReference() instanceof LinkClassification) {
                // XWADL Classification (default values)
                ClassificationModelBuilder.setAttributeDefaultValues(categoryModel,
                        ((CategoryClassification) template.getReference()).getAttributeClassificationArray());
            }
        }
        return categoryModel;
    }

    private static CategoryModel applyCategoryTemplate(CategoryModel categoryModel, CategoryType template) {
        categoryModel.setTitle(template.getTitle());
        applyAttributes(categoryModel, template.getAttributeArray());
        return categoryModel;
    }


    private static LinkModel applyLinkTemplate(LinkModel linkModel, LinkType template) {
        // TODO link + mixin template?
        linkModel.setTitle(template.getTitle());
        applyAttributes(linkModel, template.getAttributeArray());
        return linkModel;
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
