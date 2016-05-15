package de.tu_berlin.cit.intercloud.client.model.occi.convert;

import de.tu_berlin.cit.intercloud.client.exception.AttributeFormatException;
import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.occi.OcciListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryListDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.ListType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.MapItem;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.MapType;
import org.apache.xmlbeans.GDuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class provides methods to convert an {@link OcciRepresentationModel}
 * and {@link OcciListRepresentationModel} into an XML Representation.
 */
public class RepresentationModelConverter {
    private static final Logger logger = LoggerFactory.getLogger(RepresentationModelConverter.class);

    /**
     * Converts an {@link OcciListRepresentationModel} into an XML Representation.
     * @param representationListModel
     * @return
     * @throws AttributeFormatException If an Attribute could not be converted.
     */
    public static CategoryListDocument convertToXml(OcciListRepresentationModel representationListModel) throws AttributeFormatException {
        CategoryListDocument categoryListDocument = CategoryListDocument.Factory.newInstance();
        List<CategoryDocument.Category> categoryList = new ArrayList<>();
        for (OcciRepresentationModel representationModel : representationListModel.getOcciRepresentationModels()) {
            categoryList.add(convertToCategory(representationModel));
        }
        categoryListDocument.addNewCategoryList().setCategoryArray(categoryList.toArray(new CategoryDocument.Category[categoryList.size()]));
        return categoryListDocument;
    }

    /**
     * Converts an {@link OcciRepresentationModel} into an XML Representation.
     * @param representationModel
     * @return
     * @throws AttributeFormatException If an Attribute could not be converted.
     */
    public static CategoryDocument convertToXml(OcciRepresentationModel representationModel) throws AttributeFormatException {
        CategoryDocument categoryDocument = CategoryDocument.Factory.newInstance();
        categoryDocument.setCategory(convertToCategory(representationModel));
        return categoryDocument;
    }

    /**
     * Converts an {@link OcciRepresentationModel} into an XML Representation.
     * @param representationModel
     * @throws AttributeFormatException If an Attribute could not be converted.
     */
    private static CategoryDocument.Category convertToCategory(OcciRepresentationModel representationModel) throws AttributeFormatException {
        CategoryDocument.Category category = CategoryDocument.Category.Factory.newInstance();
        // KIND
        KindModel kindModel = representationModel.getKind();
        if (null != kindModel) {
            addCategoryRepresentation(category.addNewKind(), kindModel);
        }
        // MIXINs
        for (MixinModel mixinModel : representationModel.getMixins()) {
            addCategoryRepresentation(category.addNewMixin(), mixinModel);
        }

        // LINKs
        for (LinkModel linkModel : representationModel.getLinks()) {
            addLinkRepresentation(category.addNewLink(), linkModel);
        }
        return category;
    }

    /**
     * Converts a {@link LinkModel} into a {@link LinkType}.
     * @param type The resulting Link Type.
     * @param model The Link Model to be converted.
     * @throws AttributeFormatException If an Attribute could not be converted.
     */
    private static void addLinkRepresentation(LinkType type, LinkModel model) throws AttributeFormatException {
        addCategoryRepresentation(type, model);
        type.setTarget(model.getTarget());

        for (MixinModel mixinModel : model.getMixins()) {
            addCategoryRepresentation(type.addNewMixin(), mixinModel);
        }
    }

    /**
     * Converts a {@link CategoryModel} into a {@link CategoryType}.
     * @param type The resulting Category Type.
     * @param model The Category Model to be converted.
     * @throws AttributeFormatException If an Attribute could not be converted.
     */
    private static void addCategoryRepresentation(CategoryType type, CategoryModel model) throws AttributeFormatException {
        type.setSchema(model.getSchema());
        type.setTerm(model.getTerm());
        type.setTitle(model.getTitle());
        addAttributeRepresentation(type, model);
    }

    /**
     * Converts the {@link AttributeModel}s of a {@link CategoryModel} into {@link AttributeType}s
     * and adds them to the {@link CategoryType}
     * @param categoryType The Category Type where to add the converted Attribute Types.
     * @param categoryModel The Category Model containing the Attribute Models
     * @throws AttributeFormatException If an Attribute could not be converted.
     */
    private static void addAttributeRepresentation(CategoryType categoryType, CategoryModel categoryModel) throws AttributeFormatException {
        for (AttributeModel attributeModel : categoryModel.getAttributes()) {
            if (attributeModel.isMutable() && attributeModel.isRequired() && !attributeModel.hasValue()) {
                throw new IllegalArgumentException("Attribute is required but not set. " + attributeModel);
            } else if (attributeModel.isMutable() && attributeModel.hasValue()) {
                try {
                    AttributeType attributeType = categoryType.addNewAttribute();
                    attributeType.setName(attributeModel.getName());
                    switch (attributeModel.getType()) {
                        case STRING:
                            attributeType.setSTRING(attributeModel.getString());
                            break;
                        case ENUM:
                            attributeType.setENUM(attributeModel.getEnum());
                            break;
                        case INTEGER:
                            attributeType.setINTEGER(attributeModel.getInteger());
                            break;
                        case DOUBLE:
                            attributeType.setDOUBLE(attributeModel.getDouble());
                            break;
                        case FLOAT:
                            attributeType.setFLOAT(attributeModel.getFloat());
                            break;
                        case BOOLEAN:
                            attributeType.setBOOLEAN(attributeModel.getBoolean());
                            break;
                        case URI:
                            attributeType.setURI(attributeModel.getUri());
                            break;
                        case DATETIME:
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(attributeModel.getDatetime());
                            attributeType.setDATETIME(calendar);
                            break;
                        case DURATION:
                            attributeType.setDURATION(new GDuration(attributeModel.getDuration().toString()));
                            break;
                        case LIST:
                            ListType listType = ListType.Factory.newInstance();
                            listType.setItemArray((String[]) attributeModel.getList().toArray());
                            attributeType.setLIST(listType);
                            break;
                        case MAP:
                            Set<Map.Entry<String, String>> entries = attributeModel.getMap().entrySet();
                            MapType mapType = MapType.Factory.newInstance();
                            for (Map.Entry<String, String> entry : entries) {
                                MapItem item = mapType.addNewItem();
                                item.setKey(entry.getKey());
                                item.setStringValue(entry.getValue());
                            }
                            attributeType.setMAP(mapType);
                            break;
                        case SIGNATURE:
                            byte[] signature = Base64.getEncoder().encode(attributeModel.getSignature().getBytes());
                            attributeType.setSIGNATURE(signature);
                            break;
                        case KEY:
                            byte[] key = Base64.getEncoder().encode(attributeModel.getKey().getBytes());
                            attributeType.setKEY(key);
                            break;
                        default:
                            throw new AttributeFormatException("Could not set attribute representation: type is not supported " + attributeModel);
                    }
                } catch (Exception e) {
                    throw new AttributeFormatException(attributeModel.toString(), e);
                }
            }
        }

    }
}
