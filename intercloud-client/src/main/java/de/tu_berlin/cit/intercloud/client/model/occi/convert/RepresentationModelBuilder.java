package de.tu_berlin.cit.intercloud.client.model.occi.convert;

import de.tu_berlin.cit.intercloud.client.model.occi.IMixinModelContainer;
import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.ClassificationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.occi.OcciListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryListDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.MapItem;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.MapType;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepresentationModelBuilder {
    private static final Logger logger = LoggerFactory.getLogger(RepresentationModelBuilder.class);

    /**
     * Converts a {@link MapType} to a {@link Map<String, String>}.
     */
    public static Map<String, String> mapTypeToMap(MapType mapType) {
        Map<String, String> map = new HashMap<>();
        MapItem[] itemArray = mapType.getItemArray();
        if (null != itemArray && 0 < itemArray.length) {
            for (MapItem item : itemArray) {
                map.put(item.getKey(), item.getStringValue());
            }
        }
        return map;
    }

    /*
     *  REQUEST REPRESENTATION MODEL
     */

    public static OcciRepresentationModel build(ClassificationModel classificationModel) {

        OcciRepresentationModel representationModel = new OcciRepresentationModel();
        representationModel.setKind(classificationModel.getKind());
        representationModel.getLinkDefinitions().addAll(classificationModel.getLinks());

        // list of mixins that apply to other mixins
        List<MixinModel> mixinAppliesMixin = new ArrayList<>();
        // key = mixin.id | value = list of all containers that contain this mixin
        Map<String, List<IMixinModelContainer>> mixinContainersMap = new HashMap<>();

        // apply mixins to representation and links
        // collect mixins that apply to other mixins
        for (MixinModel mixin : classificationModel.getMixins()) {
            if (null == mixin.getApplies()) {
                logger.warn("Mixin missing applies. {}", mixin);
            } else if ((Category.CategorySchema + Category.CategoryTerm).equals(mixin.getApplies())) {
                // default
                List<IMixinModelContainer> mixinContainers = new ArrayList<>();
                // apply mixin to representation
                representationModel.addMixin(mixin);
                mixinContainers.add(representationModel);
                // clone mixin to all links
                for (LinkModel link : classificationModel.getLinks()) {
                    MixinModel clone = SerializationUtils.clone(mixin);
                    link.addMixin(clone);
                    mixinContainers.add(link);
                }
                mixinContainersMap.put(mixin.getId(), mixinContainers);
            } else if (null != classificationModel.getMixin(mixin.getApplies())) {
                // applies to Mixin?
                if (!addMixinModelToContainer(mixinContainersMap, mixin)) {
                    // if mixin does not apply to an already "known" mixin
                    // double check the mixin in the second run
                    mixinAppliesMixin.add(mixin);
                }
            } else if (null != classificationModel.getKind() && classificationModel.getKind().getId().equals(mixin.getApplies())) {
                // applies to Kind?
                representationModel.addMixin(mixin);
                mixinContainersMap.put(mixin.getId(), Arrays.asList(representationModel));
            } else if (null != classificationModel.getLink(mixin.getApplies())) {
                // applies to Link?
                LinkModel link = classificationModel.getLink(mixin.getApplies());
                link.addMixin(mixin);
                mixinContainersMap.put(mixin.getId(), Arrays.asList(link));
            } else {
                // applies to none
                logger.warn("Mixin does not apply to a Category. {}", mixin);
            }
        }

        // mixinAppliesMixin stuff
        int size = 0;
        while (size != mixinAppliesMixin.size()) {
            size = mixinAppliesMixin.size();
            for (int i = 0; i < mixinAppliesMixin.size(); ) {
                MixinModel mixin = mixinAppliesMixin.get(i);
                if (addMixinModelToContainer(mixinContainersMap, mixin)) {
                    // mixin applies to an already "known" mixin
                    mixinAppliesMixin.remove(i);
                } else {
                    // mixin applies to an not yet "known" mixin
                    i++;
                }
            }
        }
        if (!mixinAppliesMixin.isEmpty()) {
            logger.warn("Some mixins could not be applied. {}", mixinAppliesMixin);
        }

        return representationModel;
    }

    private static boolean addMixinModelToContainer(Map<String, List<IMixinModelContainer>> mixinContainerMap, MixinModel mixin) {
        List<IMixinModelContainer> mixinContainers = mixinContainerMap.get(mixin.getApplies());
        if (null != mixinContainers && !mixinContainers.isEmpty()) {
            // add container
            mixinContainerMap.put(mixin.getId(), mixinContainers);
            // apply first
            mixinContainers.get(0).addMixin(mixin);
            // clone others
            for (int k = 1; k < mixinContainers.size(); k++) {
                MixinModel clone = SerializationUtils.clone(mixin);
                mixinContainers.get(k).addMixin(clone);
            }
            return true;
        } else {
            return false;
        }
    }

    /*
     * RESPONSE REPRESENTATION MODEL
     */

    public static OcciListRepresentationModel build(CategoryListDocument categoryListDocument, ClassificationModel classificationModel) {
        CategoryListDocument.CategoryList categoryList = categoryListDocument.getCategoryList();
        List<OcciRepresentationModel> representationList = new ArrayList<>();

        for (CategoryDocument.Category category : categoryList.getCategoryArray()) {
            representationList.add(build(category, classificationModel));
        }
        return new OcciListRepresentationModel(representationList);
    }

    public static OcciRepresentationModel build(CategoryDocument categoryDocument, ClassificationModel classificationModel) {
        CategoryDocument.Category category = categoryDocument.getCategory();
        return build(category, classificationModel);
    }

    private static OcciRepresentationModel build(CategoryDocument.Category category, ClassificationModel classificationModel) {
        KindModel kind = null;
        List<MixinModel> mixinList = new ArrayList<>();
        List<LinkModel> linkList = new ArrayList<>();

        if (null != category.getKind()) {
            kind = buildKindModel(category.getKind(), classificationModel);
        }
        if (null != category.getMixinArray() && 0 < category.getMixinArray().length) {
            for (CategoryType mixinType : category.getMixinArray()) {
                mixinList.add(buildMixinModel(mixinType, classificationModel));
            }

        }
        if (null != category.getLinkArray() && 0 < category.getLinkArray().length) {
            for (LinkType linkType : category.getLinkArray()) {
                linkList.add(buildLinkModel(linkType, classificationModel));
            }

        }

        return new OcciRepresentationModel(kind, mixinList, linkList);
    }

    private static KindModel buildKindModel(CategoryType kindType, ClassificationModel classificationModel) {
        String kindId = kindType.getSchema() + kindType.getTerm();
        KindModel kindModel = null != classificationModel ? classificationModel.getKind() : null;
        KindModel result = new KindModel(kindType.getSchema(), kindType.getTerm());
        result.setTitle(kindType.getTitle());
        buildAttributes(result, kindModel, kindType.getAttributeArray());
        if (null == kindModel || !kindModel.getId().equals(kindId)) {
            logger.warn("Rest document contains Kind, but Kind not defined in Classification. kind: {}", kindId);
        }

        return result;
    }

    private static MixinModel buildMixinModel(CategoryType mixinType, ClassificationModel classificationModel) {
        String mixinId = mixinType.getSchema() + mixinType.getTerm();
        MixinModel mixinModel = null != classificationModel ? classificationModel.getMixin(mixinId) : null;
        MixinModel result;
        if (null == mixinModel) {
            result = new MixinModel(mixinType.getSchema(), mixinType.getTerm(), null);
            logger.warn("Rest document contains Mixin, but Mixin not defined in Classification, mixin: {}", mixinId);
        } else {
            result = new MixinModel(mixinModel.getSchema(), mixinModel.getTerm(), mixinModel.getApplies());
        }
        result.setTitle(mixinType.getTitle());
        buildAttributes(result, mixinModel, mixinType.getAttributeArray());

        return result;
    }

    private static LinkModel buildLinkModel(LinkType linkType, ClassificationModel classificationModel) {
        String linkId = linkType.getSchema() + linkType.getTerm();
        LinkModel linkModel = null != classificationModel ? classificationModel.getLink(linkId) : null;
        LinkModel result;
        if (null == linkModel) {
            result = new LinkModel(linkType.getSchema(), linkType.getTerm(), null);
            logger.warn("Rest document contains Link, but Link not defined in Classification, link: {}", linkId);
        } else {
            result = new LinkModel(linkModel.getSchema(), linkModel.getTerm(), linkModel.getRelates());
        }
        result.setTitle(linkType.getTitle());
        result.setTarget(linkType.getTarget());
        buildAttributes(result, linkModel, linkType.getAttributeArray());

        if (null != linkType.getAttributeArray() && 0 < linkType.getMixinArray().length) {
            for (CategoryType mixinType : linkType.getMixinArray()) {
                result.addMixin(buildMixinModel(mixinType, classificationModel));
            }
        }

        return result;
    }

    private static CategoryModel buildAttributes(CategoryModel result, CategoryModel categoryModel, AttributeType[] attributeTypes) {
        if (null != attributeTypes) {
            for (AttributeType attributeType : attributeTypes) {
                AttributeModel attributeModel;
                if (null == attributeType.getName()) {
                    logger.warn("Rest document contains attribute with no name.");
                    continue;
                } else if (null == categoryModel) {
                    // attribute type to model
                    attributeModel = buildAttributeModel(attributeType);
                } else {
                    attributeModel = categoryModel.getAttribute(attributeType.getName());
                    if (null == attributeModel) {
                        // attribute type to model
                        logger.warn("Rest document contains Attribute, but Attribute not defined in Classification. category: {}, attribute: {}",
                                result.getId(), attributeType.getName());
                        attributeModel = buildAttributeModel(attributeType);
                    } else {
                        // compare attribute type to classification attributes
                        attributeModel = buildAttributeModel(attributeType, attributeModel);
                    }
                }
                if (null != attributeModel) {
                    result.addAttribute(attributeModel);
                }
            }
        }
        return result;
    }

    private static AttributeModel buildAttributeModel(AttributeType attributeType) {
        return buildAttributeModel(attributeType, null);
    }

    private static AttributeModel buildAttributeModel(AttributeType attributeType, AttributeModel attributeModel) {
        AttributeModel result = null;
        String description = null;
        boolean required = false;
        boolean mutable = false;
        if (null != attributeModel) {
            description = attributeModel.getDescription();
            required = attributeModel.isRequired();
            mutable = attributeModel.isMutable();
        }

        if (attributeType.isSetBOOLEAN()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.BOOLEAN, required, mutable, description);
            result.setBoolean(attributeType.getBOOLEAN());

        } else if (attributeType.isSetDATETIME()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.DATETIME, required, mutable, description);
            result.setDatetime(attributeType.getDATETIME().getTime());

        } else if (attributeType.isSetDOUBLE()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.DOUBLE, required, mutable, description);
            result.setDouble(attributeType.getDOUBLE());

        } else if (attributeType.isSetDURATION()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.DURATION, required, mutable, description);
            result.setDuration(Duration.parse(attributeType.getDURATION().toString()));

        } else if (attributeType.isSetENUM()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.ENUM, required, mutable, description);
            result.setEnum(attributeType.getENUM());

        } else if (attributeType.isSetFLOAT()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.FLOAT, required, mutable, description);
            result.setFloat(attributeType.getFLOAT());

        } else if (attributeType.isSetINTEGER()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.INTEGER, required, mutable, description);
            result.setInteger(attributeType.getINTEGER());

        } else if (attributeType.isSetLIST()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.LIST, required, mutable, description);
            result.setList(Arrays.asList(attributeType.getLIST().getItemArray()));

        } else if (attributeType.isSetMAP()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.MAP, required, mutable, description);
            result.setMap(mapTypeToMap(attributeType.getMAP()));

        } else if (attributeType.isSetKEY()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.KEY, required, mutable, description);
            byte[] key = Base64.getDecoder().decode(attributeType.getKEY());
            result.setKey(new String(key));

        } else if (attributeType.isSetSIGNATURE()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.SIGNATURE, required, mutable, description);
            byte[] signature = Base64.getDecoder().decode(attributeType.getKEY());
            result.setSignature(new String(signature));

        } else if (attributeType.isSetSTRING()) {
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.STRING, required, mutable, description);
            result.setString(attributeType.getSTRING());

        } else if (attributeType.isSetURI()) {
            result = new AttributeModel(attributeType.getURI(), AttributeModel.Type.URI, required, mutable, description);
            result.setUri(attributeType.getURI());
        } else {
            logger.warn("Unsupported attribute type: attribute: {}", attributeType.getName());
        }

        if (null != attributeModel && null != result && !result.getType().equals(attributeModel.getType())) {
            logger.warn("Differing attribute type. attribute: {}, classification: {}, actual: {}",
                    attributeType.getName(), attributeModel.getType(), result.getType());
        }

        return result;
    }
}
