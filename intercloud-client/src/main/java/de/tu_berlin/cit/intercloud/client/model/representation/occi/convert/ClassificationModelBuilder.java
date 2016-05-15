package de.tu_berlin.cit.intercloud.client.model.representation.occi.convert;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.ClassificationModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.method.TemplateModel;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.LinkClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.MixinClassification;
import de.tu_berlin.cit.rwx4j.xwadl.GrammarsDocument;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.util.Base64;
import java.util.Calendar;

/**
 * This class provides methods to convert a {@link ClassificationDocument.Classification}
 * into a {@link ClassificationModel}.
 */
public class ClassificationModelBuilder {
    private final static Logger logger = LoggerFactory.getLogger(ClassificationModelBuilder.class);

    /**
     * Builds a {@link ClassificationModel} based on the {@link ClassificationDocument.Classification}
     * contained in the {@link GrammarsDocument.Grammars} section of an
     * {@link de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument}
     * @param grammars The grammars section containing a Classification.
     * @param setDefaultValues Whether or not to set the default Attribute Values.
     * @return
     */
    public static ClassificationModel build(GrammarsDocument.Grammars grammars, boolean setDefaultValues) {
        ClassificationModel classificationModel = null;
        ClassificationDocument.Classification classification = getClassification(grammars);
        if (null != classification) {
            classificationModel = build(classification, setDefaultValues);
        }
        return classificationModel;
    }

    /**
     * Returns the {@link ClassificationDocument.Classification} contained
     * in the {@link GrammarsDocument.Grammars} section.
     * @param grammars The grammars section containing a Classification.
     * @return
     */
    private static ClassificationDocument.Classification getClassification(GrammarsDocument.Grammars grammars) {
        ClassificationDocument.Classification result = null;
        if (null != grammars) {
            XmlObject[] classifications = grammars.selectChildren("urn:xmpp:occi-classification", "Classification");
            if (null != classifications && 0 < classifications.length) {
                result = (ClassificationDocument.Classification) classifications[0];
            }
        }
        return result;
    }

    /**
     * Builds a {@link ClassificationModel} based on a {@link ClassificationDocument.Classification}.
     * @param classification The Classification.
     * @param setDefaultValues Whether or not to set the default Attribute Values.
     * @return
     */
    public static ClassificationModel build(ClassificationDocument.Classification classification, boolean setDefaultValues) {
        ClassificationModel classificationModel = new ClassificationModel();
        // read kind from classification
        if (null != classification.getKindType()) {
            classificationModel.setKind(buildKindModel(classification.getKindType(), setDefaultValues));
        }
        // read links from classification
        if (null != classification.getLinkTypeArray() && 0 < classification.getLinkTypeArray().length) {
            for (LinkClassification c : classification.getLinkTypeArray()) {
                classificationModel.addLink(buildLinkModel(c, setDefaultValues));
            }
        }
        // read mixins from classification
        if (null != classification.getMixinTypeArray() && 0 < classification.getMixinTypeArray().length) {
            for (MixinClassification c : classification.getMixinTypeArray()) {
                classificationModel.addMixin(buildMixinModel(c, setDefaultValues));
            }
        }

        return classificationModel;
    }

    /**
     * Builds a {@link KindModel} based on the Kind definition
     * of a {@link ClassificationDocument.Classification}.
     * @param classification The Kind XML definition.
     * @param setDefaultValues Whether or not to set the default Attribute Values.
     * @return
     */
    private static KindModel buildKindModel(CategoryClassification classification, boolean setDefaultValues) {
        KindModel model = new KindModel(classification.getSchema(), classification.getTerm());
        model.setTitle(classification.getTitle());
        if (buildAttributeModels(model, classification.getAttributeClassificationArray(), setDefaultValues)) {
            // only add kind definition to template if default values are present
            model.addTemplate(new TemplateModel(model.getTitle(), classification));
        } else {
            // if no default values are given, set the definition to none
            model.addTemplate(new TemplateModel(model.getTitle(), null));
        }
        return model;
    }

    /**
     * Builds a {@link MixinModel} based on the Mixin definition
     * of a {@link ClassificationDocument.Classification}.
     * @param classification The Mixin XML definition
     * @param setDefaultValues Whether or not to set the default Attribute Values.
     * @return
     */
    private static MixinModel buildMixinModel(MixinClassification classification, boolean setDefaultValues) {
        MixinModel model = new MixinModel(classification.getSchema(), classification.getTerm(), classification.getApplies());
        model.setTitle(classification.getTitle());
        if (buildAttributeModels(model, classification.getAttributeClassificationArray(), setDefaultValues)) {
            model.addTemplate(new TemplateModel(model.getTitle(), classification));
        } else {
            model.addTemplate(new TemplateModel(model.getTitle(), null));
        }
        return model;
    }

    /**
     * Builds a {@link LinkModel} based on the Link definition
     * of a {@link ClassificationDocument.Classification}.
     * @param classification The Link XML definition
     * @param setDefaultValues Whether or not to set the default Attribute Values.
     * @return
     */
    private static LinkModel buildLinkModel(LinkClassification classification, boolean setDefaultValues) {
        LinkModel model = new LinkModel(classification.getSchema(), classification.getTerm(), classification.getRelation());
        model.setTitle(classification.getTitle());
        if (buildAttributeModels(model, classification.getAttributeClassificationArray(), setDefaultValues)) {
            model.addTemplate(new TemplateModel(model.getTitle(), classification));
        } else {
            model.addTemplate(new TemplateModel(model.getTitle(), null));
        }
        return model;
    }

    /**
     * Builds and adds the {@link AttributeModel} list to a {@link CategoryModel}
     * based on the corresponding XML object.
     * @param categoryModel The CategoryModel where to add the Attributes.
     * @param attributeClassifications List ob Attribute XML objects.
     * @param setDefaultValues Whether or not to set the default Attribute Values.
     * @return True if any Attribute contains a default value, False otherwise.
     */
    private static boolean buildAttributeModels(CategoryModel categoryModel,
                                                AttributeClassificationDocument.AttributeClassification[] attributeClassifications,
                                                boolean setDefaultValues) {
        boolean hasDefaultValue = false;
        if (null != attributeClassifications && 0 < attributeClassifications.length) {
            for (AttributeClassificationDocument.AttributeClassification a : attributeClassifications) {
                AttributeModel attributeModel = new AttributeModel(a.getName(), a.getType().toString(), a.getRequired(), a.getMutable(), a.getDescription());
                categoryModel.addAttribute(attributeModel);
                if (setDefaultValues) {
                    addAttributeDefaultValue(attributeModel, a);
                    hasDefaultValue |= attributeModel.hasValue();
                }
            }
        }
        return hasDefaultValue;
    }

    /**
     * Sets the default value of an {@link AttributeModel} based on the
     * {@link AttributeClassificationDocument.AttributeClassification}
     * @param attributeModel
     * @param attributeClassification
     * @return
     */
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
                        byte[] signature = Base64.getDecoder().decode(defaultValue);
                        attributeModel.setSignature(new String(signature));
                        break;
                    case KEY:
                        byte[] key = Base64.getDecoder().decode(defaultValue);
                        attributeModel.setKey(new String(key));
                        break;
                    case LIST:
                    case MAP:
                    default:
                        // ignore List & MAP - log warning
                        logger.warn("Could not set default value of classification attribute. {}, value: {}", attributeModel, defaultValue);
                }
            } catch (Exception e) {
                logger.error("Failed to parse default value of classification attribute. {}, value: {}", attributeModel, defaultValue);
            }
        }
        return attributeModel;
    }

    /**
     * Set the {@link AttributeModel} list of a {@link CategoryModel} to default values.
     * @param categoryModel The CategoryModel containing the AttributeModel where to set the default values.
     * @param attributeClassifications The AttributeClassification defining the default values.
     */
    public static void setAttributeDefaultValues(CategoryModel categoryModel,
                                                 AttributeClassificationDocument.AttributeClassification[] attributeClassifications) {
        for (AttributeClassificationDocument.AttributeClassification attributeClassification : attributeClassifications) {
            AttributeModel attributeModel = categoryModel.getAttribute(attributeClassification.getName());
            if (null != attributeModel) {
                addAttributeDefaultValue(attributeModel, attributeClassification);
            }
        }
    }
}
