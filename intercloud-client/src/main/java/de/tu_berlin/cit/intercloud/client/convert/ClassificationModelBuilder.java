package de.tu_berlin.cit.intercloud.client.convert;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.ClassificationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.LinkClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.MixinClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.util.Calendar;

public class ClassificationModelBuilder {
    private final static Logger logger = LoggerFactory.getLogger(ClassificationModelBuilder.class);

    public static ClassificationModel build(ClassificationDocument.Classification classificationDocument) {
        ClassificationModel classificationModel = new ClassificationModel();
        // read kind from classification
        if (null != classificationDocument.getKindType()) {
            classificationModel.setKind(buildKindModel(classificationDocument.getKindType()));
        }
        // read links from classification
        if (null != classificationDocument.getLinkTypeArray() && 0 < classificationDocument.getLinkTypeArray().length) {
            for (LinkClassification c : classificationDocument.getLinkTypeArray()) {
                classificationModel.addLink(buildLinkModel(c));
            }
        }
        // read mixins from classification
        if (null != classificationDocument.getMixinTypeArray() && 0 < classificationDocument.getMixinTypeArray().length) {
            for (MixinClassification c : classificationDocument.getMixinTypeArray()) {
                classificationModel.addMixin(buildMixinModel(c));
            }
        }

        return classificationModel;
    }

    private static KindModel buildKindModel(CategoryClassification classification) {
        KindModel model = new KindModel(classification.getSchema(), classification.getTerm());
        model.setTitle(classification.getTitle());
        buildAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private static MixinModel buildMixinModel(MixinClassification classification) {
        MixinModel model = new MixinModel(classification.getSchema(), classification.getTerm(), classification.getApplies());
        model.setTitle(classification.getTitle());
        buildAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private static LinkModel buildLinkModel(LinkClassification classification) {
        LinkModel model = new LinkModel(classification.getSchema(), classification.getTerm(), classification.getRelation());
        model.setTitle(classification.getTitle());
        buildAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private static CategoryModel buildAttributeModels(CategoryModel categoryModel,
                                                      AttributeClassificationDocument.AttributeClassification[] attributeClassifications) {
        if (null != attributeClassifications && 0 < attributeClassifications.length) {
            for (AttributeClassificationDocument.AttributeClassification a : attributeClassifications) {
                AttributeModel attributeModel = new AttributeModel(a.getName(), a.getType().toString(), a.getRequired(), a.getMutable(), a.getDescription());
                addAttributeDefaultValue(attributeModel, a);
                categoryModel.addAttribute(attributeModel);
            }
        }
        return categoryModel;
    }

    private static AttributeModel addAttributeDefaultValue(AttributeModel attributeModel,
                                                           AttributeClassificationDocument.AttributeClassification attributeClassification) {
        String defaultValue = attributeClassification.getDefault();
        if (null != defaultValue && !defaultValue.trim().isEmpty()) {
            try {
                switch (attributeModel.getType()) {
                    case STRING:
                        attributeModel.setString(defaultValue);
                        break;
                    case ENUM:
                        attributeModel.setEnum(defaultValue);
                        break;
                    case URI:
                        attributeModel.setUri(defaultValue);
                        break;
                    case INTEGER:
                        attributeModel.setInteger(Integer.parseInt(defaultValue));
                        break;
                    case DOUBLE:
                        attributeModel.setDouble(Double.parseDouble(defaultValue));
                        break;
                    case FLOAT:
                        attributeModel.setFloat(Float.parseFloat(defaultValue));
                        break;
                    case BOOLEAN:
                        attributeModel.setBoolean(Boolean.parseBoolean(defaultValue));
                        break;
                    case DURATION:
                        attributeModel.setDuration(Duration.parse(defaultValue));
                        break;
                    case DATETIME:
                        Calendar calendar = DatatypeConverter.parseDateTime(defaultValue);
                        attributeModel.setDatetime(calendar.getTime());
                        break;
                    case SIGNATURE:
                    case KEY:
                    case LIST:
                    case MAP:
                    default:
                        // TODO string representation
                        logger.warn("Could not set default value of classification attribute. {}, value: {}", attributeModel, defaultValue);
                }
            } catch (Exception e) {
                logger.error("Failed to parse default value of classification attribute. {}, value: {}", attributeModel, defaultValue);
            }
        }
        return attributeModel;
    }
}
