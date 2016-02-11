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

import java.util.Base64;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class RepresentationModelConverter {
    private static final Logger logger = LoggerFactory.getLogger(RepresentationModelConverter.class);

    public static CategoryListDocument convertToXml(OcciListRepresentationModel representationListModel) throws AttributeFormatException {
        CategoryListDocument categoryListDocument = CategoryListDocument.Factory.newInstance();
        CategoryListDocument.CategoryList categoryList = categoryListDocument.addNewCategoryList();
        for (OcciRepresentationModel representationModel : representationListModel.getOcciRepresentationModels()) {
            convertToXml(representationModel, categoryList.addNewCategory());
        }
        return categoryListDocument;
    }

    public static CategoryDocument convertToXml(OcciRepresentationModel representationModel) throws AttributeFormatException {
        CategoryDocument categoryDocument = CategoryDocument.Factory.newInstance();
        CategoryDocument.Category category = categoryDocument.addNewCategory();
        convertToXml(representationModel, category);
        return categoryDocument;
    }

    private static CategoryDocument.Category convertToXml(OcciRepresentationModel representationModel, CategoryDocument.Category category) throws AttributeFormatException {
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

    private static void addLinkRepresentation(LinkType type, LinkModel model) throws AttributeFormatException {
        addCategoryRepresentation(type, model);
        type.setTarget(model.getTarget());

        for (MixinModel mixinModel : model.getMixins()) {
            addCategoryRepresentation(type.addNewMixin(), mixinModel);
        }
    }

    private static void addCategoryRepresentation(CategoryType type, CategoryModel model) throws AttributeFormatException {
        type.setSchema(model.getSchema());
        type.setTerm(model.getTerm());
        type.setTitle(model.getTitle());
        addAttributeRepresentation(type, model);
    }

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
                        case KEY:
                            byte[] key = Base64.getEncoder().encode(attributeModel.getKey().getBytes());
                            attributeType.setKEY(key);
                        default:
                            throw new AttributeFormatException("Could not set attribute representation: type is not supported " + attributeModel);
                    }
                } catch (Exception e) {
                    throw new AttributeFormatException(attributeModel.toString(), e);
                }
            }
        }

    }

    private static boolean hasAttributes(CategoryModel category) {
        for (AttributeModel a : category.getAttributes()) {
            if (a.isMutable() && (a.isRequired() || a.hasValue())) {
                return true;
            }
        }
        return false;
    }
}
