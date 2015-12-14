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

public class ClassificationModelBuilder {

    public static ClassificationModel build(ClassificationDocument.Classification classificationDocument) {
        ClassificationModel classificationModel = new ClassificationModel();
        // TODO default values
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
        buildAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private static MixinModel buildMixinModel(MixinClassification classification) {
        MixinModel model = new MixinModel(classification.getSchema(), classification.getTerm(), classification.getApplies());
        buildAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private static LinkModel buildLinkModel(LinkClassification classification) {
        LinkModel model = new LinkModel(classification.getSchema(), classification.getTerm(), classification.getRelation());
        buildAttributeModels(model, classification.getAttributeClassificationArray());
        return model;
    }

    private static CategoryModel buildAttributeModels(CategoryModel categoryModel, AttributeClassificationDocument.AttributeClassification[] attributeClassifications) {
        if (null != attributeClassifications && 0 < attributeClassifications.length) {
            for (AttributeClassificationDocument.AttributeClassification a : attributeClassifications) {
                categoryModel.addAttribute(new AttributeModel(a.getName(), a.getType().toString(), a.getRequired(), a.getMutable(), a.getDescription()));
            }
        }
        return categoryModel;
    }
}
