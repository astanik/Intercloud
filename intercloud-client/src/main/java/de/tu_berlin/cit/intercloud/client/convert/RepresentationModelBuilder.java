package de.tu_berlin.cit.intercloud.client.convert;

import de.tu_berlin.cit.intercloud.client.model.IMixinModelContainer;
import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.ClassificationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepresentationModelBuilder {
    private static final Logger logger = LoggerFactory.getLogger(RepresentationModelBuilder.class);

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
                logger.error("Mixin missing applies. {}", mixin);
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
                mixinAppliesMixin.add(mixin);
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

    public static OcciRepresentationModel build(ClassificationModel classificationModel, CategoryDocument categoryDocument) {
        CategoryDocument.Category category = categoryDocument.getCategory();
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
        KindModel kindModel = classificationModel.getKind();
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
        MixinModel mixinModel = classificationModel.getMixin(mixinId);
        MixinModel result;
        if (null == mixinModel || !mixinModel.getId().equals(mixinId)) {
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
        LinkModel linkModel = classificationModel.getLink(linkId);
        LinkModel result;
        if (null == linkModel || !linkModel.getId().equals(linkId)) {
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
                } else if (null == categoryModel.getAttribute(attributeType.getName())) {
                    // attribute type to model
                    logger.warn("Rest document contains Attribute, but Attribute not defined in Classification. category: {}, attribute: {}",
                            result.getId(), attributeType.getName());
                    attributeModel = buildAttributeModel(attributeType);
                } else {
                    // compare attribute type to classification attributes
                    attributeModel = buildAttributeModel(attributeType, categoryModel.getAttribute(attributeType.getName()));
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
        String description = null != attributeModel ? attributeModel.getDescription() : null;
        AttributeModel.Type actualType = null; // just for logging, befor return

        if (attributeType.isSetBOOLEAN()) {
            if (null != attributeModel && !AttributeModel.Type.BOOLEAN.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.BOOLEAN;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.BOOLEAN, false, false, description);
            result.setBoolean(attributeType.getBOOLEAN());

        } else if (attributeType.isSetDATETIME()) {
            if (null != attributeModel && !AttributeModel.Type.DATETIME.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.DATETIME;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.DATETIME, false, false, description);
            result.setDatetime(attributeType.getDATETIME().getTime());

        } else if (attributeType.isSetDOUBLE()) {
            if (null != attributeModel && !AttributeModel.Type.DOUBLE.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.DOUBLE;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.DOUBLE, false, false, description);
            result.setDouble(attributeType.getDOUBLE());

        } else if (attributeType.isSetDURATION()) {
            if (null != attributeModel && !AttributeModel.Type.DURATION.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.DURATION;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.DURATION, false, false, description);
            result.setDuration(Duration.parse(attributeType.getDURATION().toString()));

        } else if (attributeType.isSetENUM()) {
            if (null != attributeModel && !AttributeModel.Type.ENUM.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.ENUM;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.ENUM, false, false, description);
            result.setEnum(attributeType.getENUM());

        } else if (attributeType.isSetFLOAT()) {
            if (null != attributeModel && !AttributeModel.Type.FLOAT.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.FLOAT;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.FLOAT, false, false, description);
            result.setFloat(attributeType.getFLOAT());

        } else if (attributeType.isSetINTEGER()) {
            if (null != attributeModel && !AttributeModel.Type.INTEGER.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.INTEGER;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.INTEGER, false, false, description);
            result.setInteger(attributeType.getINTEGER());

        } else if (attributeType.isSetKEY()) {
            if (null != attributeModel && !AttributeModel.Type.KEY.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.KEY;
            }
            // TODO
            logger.warn("Unsupported attribute type: KEY, {}", attributeType.getName());
        } else if (attributeType.isSetLIST()) {
            if (null != attributeModel && !AttributeModel.Type.LIST.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.LIST;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.LIST, false, false, description);
            result.setList(Arrays.asList(attributeType.getLIST().getItemArray()));

        } else if (attributeType.isSetMAP()) {
            if (null != attributeModel && !AttributeModel.Type.MAP.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.MAP;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.MAP, false, false, description);
            result.setMap(mapTypeToMap(attributeType.getMAP()));

        } else if (attributeType.isSetSIGNATURE()) {
            if (null != attributeModel && !AttributeModel.Type.SIGNATURE.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.SIGNATURE;
            }
            // TODO
            logger.warn("Unsupported attribute type: KEY, {}", attributeType.getName());

        } else if (attributeType.isSetSTRING()) {
            if (null != attributeModel && !AttributeModel.Type.STRING.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.STRING;
            }
            result = new AttributeModel(attributeType.getName(), AttributeModel.Type.STRING, false, false, description);
            result.setString(attributeType.getSTRING());

        } else if (attributeType.isSetURI()) {
            if (null != attributeModel && !AttributeModel.Type.URI.equals(attributeModel.getType())) {
                actualType = AttributeModel.Type.URI;
            }
            result = new AttributeModel(attributeType.getURI(), AttributeModel.Type.URI, false, false, description);
            result.setUri(attributeType.getURI());
        } else {
            logger.warn("Unsupported attribute type: attribute: {}", attributeType.getName());
        }

        if (null != actualType && null != attributeModel) {
            logger.warn("Differing attribute type. attribute: {}, classification: {}, actual: {}",
                    attributeType.getName(), attributeModel.getType(), actualType);
        }

        return result;
    }
}
