package de.tu_berlin.cit.intercloud.client.model.occi.convert;

import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.ClassificationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildOcciRepresentationTest {
    private static final String SCHEMA = "example.schema.de/cit#";

    private ClassificationModel classificationModel;

    @Before
    public void before() {
        classificationModel = new ClassificationModel();
    }

    @After
    public void after() {
        classificationModel = null;
    }

    private <T extends CategoryModel> Map<String, T > newCategoryModelMap(T... categoryModels) {
        return newCategoryModelMap(Arrays.asList(categoryModels));
    }

    private <T extends CategoryModel> Map<String, T> newCategoryModelMap(Collection<T> categoryModels) {
        Map<String, T> categoryModelMap = new HashMap<>();
        for (T c : categoryModels) {
            categoryModelMap.put(c.getId(), c);
        }
        return categoryModelMap;
    }

    @Test
    public void emptyClassification() {
        OcciRepresentationModel representationModel = RepresentationModelBuilder.build(classificationModel);
        Assert.assertNull(representationModel.getKind());
        Assert.assertTrue(representationModel.getLinkDefinitions().isEmpty());
        Assert.assertTrue(representationModel.getMixins().isEmpty());
    }

    @Test
    public void noMixins() {
        KindModel kind = new KindModel(SCHEMA, "k");
        classificationModel.setKind(kind);
        LinkModel link1 = new LinkModel(SCHEMA, "l1", null);
        classificationModel.addLink(link1);
        LinkModel link2 = new LinkModel(SCHEMA, "l2", null);
        classificationModel.addLink(link2);

        OcciRepresentationModel representationModel = RepresentationModelBuilder.build(classificationModel);
        Assert.assertEquals(kind, representationModel.getKind());
        Assert.assertEquals(2, representationModel.getLinkDefinitions().size());
        Assert.assertTrue(representationModel.getLinkDefinitions().contains(link1));
        Assert.assertTrue(representationModel.getLinkDefinitions().contains(link2));
    }


    @Test
    public void kindWithMixins() {
        KindModel kind = new KindModel(SCHEMA, "k");
        classificationModel.setKind(kind);
        MixinModel mixin1 = new MixinModel(SCHEMA, "m1", kind.getId());
        classificationModel.addMixin(mixin1);
        MixinModel mixin2 = new MixinModel(SCHEMA, "m2", kind.getId());
        classificationModel.addMixin(mixin2);

        OcciRepresentationModel representationModel = RepresentationModelBuilder.build(classificationModel);
        Assert.assertEquals(kind, representationModel.getKind());
        Assert.assertTrue(representationModel.getLinkDefinitions().isEmpty());
        Assert.assertEquals(2, representationModel.getMixins().size());
        Assert.assertTrue(representationModel.getMixins().contains(mixin1));
        Assert.assertTrue(representationModel.getMixins().contains(mixin2));
    }


    @Test
    public void kindWithMixinsWithMixins() {
        KindModel kind = new KindModel(SCHEMA, "k");
        classificationModel.setKind(kind);
        MixinModel mixin1 = new MixinModel(SCHEMA, "m1", kind.getId());
        classificationModel.addMixin(mixin1);
        MixinModel mixin11 = new MixinModel(SCHEMA, "m11", mixin1.getId());
        classificationModel.addMixin(mixin11);
        MixinModel mixin12 = new MixinModel(SCHEMA, "m12", mixin1.getId());
        classificationModel.addMixin(mixin12);
        MixinModel mixin121 = new MixinModel(SCHEMA, "m121", mixin12.getId());
        classificationModel.addMixin(mixin121);

        MixinModel mixin2 = new MixinModel(SCHEMA, "m2", kind.getId());
        classificationModel.addMixin(mixin2);
        MixinModel mixin21 = new MixinModel(SCHEMA, "m21", mixin2.getId());
        classificationModel.addMixin(mixin21);
        MixinModel mixin211 = new MixinModel(SCHEMA, "m211", mixin21.getId());
        classificationModel.addMixin(mixin211);

        List<MixinModel> mixinList = Arrays.asList(mixin1, mixin11, mixin12, mixin121, mixin2, mixin21, mixin211);

        OcciRepresentationModel representationModel = RepresentationModelBuilder.build(classificationModel);
        Assert.assertEquals(kind, representationModel.getKind());
        Assert.assertTrue(representationModel.getLinkDefinitions().isEmpty());
        Assert.assertEquals(7, representationModel.getMixins().size());
        Assert.assertTrue(representationModel.getMixins().containsAll(mixinList));
    }

    @Test
    public void mixinWithMixins() {
        MixinModel mixin1 = new MixinModel(SCHEMA, "m1", Category.CategorySchema + Category.CategoryTerm);
        classificationModel.addMixin(mixin1);
        MixinModel mixin11 = new MixinModel(SCHEMA, "m11", mixin1.getId());
        classificationModel.addMixin(mixin11);
        MixinModel mixin111 = new MixinModel(SCHEMA, "m111", mixin11.getId());
        classificationModel.addMixin(mixin111);
        MixinModel mixin1111 = new MixinModel(SCHEMA, "m1111", mixin111.getId());
        classificationModel.addMixin(mixin1111);
        MixinModel mixin12 = new MixinModel(SCHEMA, "m12", mixin1.getId());
        classificationModel.addMixin(mixin12);

        List<MixinModel> mixinList = Arrays.asList(mixin1, mixin11, mixin111, mixin1111, mixin12);
        OcciRepresentationModel representationModel = RepresentationModelBuilder.build(classificationModel);
        Assert.assertNull(representationModel.getKind());
        Assert.assertTrue(representationModel.getLinkDefinitions().isEmpty());
        Assert.assertEquals(5, representationModel.getMixins().size());
        Assert.assertTrue(representationModel.getMixins().containsAll(mixinList));
    }

    @Test
    public void linksWithMixins() {
        LinkModel link1 = new LinkModel(SCHEMA, "l1", null);
        classificationModel.addLink(link1);
        MixinModel mixin11 = new MixinModel(SCHEMA, "m11", link1.getId());
        classificationModel.addMixin(mixin11);
        MixinModel mixin111 = new MixinModel(SCHEMA, "m111", mixin11.getId());
        classificationModel.addMixin(mixin111);
        MixinModel mixin112 = new MixinModel(SCHEMA, "m112", mixin11.getId());
        classificationModel.addMixin(mixin112);
        MixinModel mixin12 = new MixinModel(SCHEMA, "m12", link1.getId());
        classificationModel.addMixin(mixin12);

        LinkModel link2 = new LinkModel(SCHEMA, "l2", null);
        classificationModel.addLink(link2);
        MixinModel mixin21 = new MixinModel(SCHEMA, "m21", link2.getId());
        classificationModel.addMixin(mixin21);

        OcciRepresentationModel representationModel = RepresentationModelBuilder.build(classificationModel);
        Assert.assertNull(representationModel.getKind());
        Assert.assertTrue(representationModel.getMixins().isEmpty());
        Assert.assertEquals(2, representationModel.getLinkDefinitions().size());
        Assert.assertTrue(representationModel.getLinkDefinitions().contains(link1));
        Assert.assertTrue(representationModel.getLinkDefinitions().contains(link2));
        Assert.assertEquals(4, link1.getMixins().size());
        Assert.assertTrue(link1.getMixins().containsAll(Arrays.asList(mixin11, mixin111, mixin112, mixin12)));
        Assert.assertEquals(1, link2.getMixins().size());
        Assert.assertTrue(link2.getMixins().contains(mixin21));
    }

    @Test
    public void defaultMixin() {
        LinkModel link1 = new LinkModel(SCHEMA, "l1", null);
        classificationModel.addLink(link1);
        LinkModel link2 = new LinkModel(SCHEMA, "l2", null);
        classificationModel.addLink(link2);

        MixinModel mixin1 = new MixinModel(SCHEMA, "m1", Category.CategorySchema + Category.CategoryTerm);
        classificationModel.addMixin(mixin1);
        MixinModel mixin11 = new MixinModel(SCHEMA, "m11", mixin1.getId());
        classificationModel.addMixin(mixin11);
        MixinModel mixin12 = new MixinModel(SCHEMA, "m12", mixin1.getId());
        classificationModel.addMixin(mixin12);

        OcciRepresentationModel representationModel = RepresentationModelBuilder.build(classificationModel);
        Assert.assertNull(representationModel.getKind());
        Assert.assertEquals(2, representationModel.getLinkDefinitions().size());
        Assert.assertTrue(representationModel.getLinkDefinitions().containsAll(Arrays.asList(link1, link2)));
        Assert.assertEquals(3, representationModel.getMixins().size());
        Assert.assertTrue(representationModel.getMixins().containsAll(Arrays.asList(mixin1, mixin11, mixin12)));
        Assert.assertEquals(3, link1.getMixins().size());
        Assert.assertTrue(link1.getMixins().containsAll(Arrays.asList(mixin1, mixin11, mixin12)));
        Assert.assertEquals(3, link2.getMixins().size());
        Assert.assertTrue(link2.getMixins().containsAll(Arrays.asList(mixin1, mixin11, mixin12)));
    }

    @Test
    public void mixinAppliesToNone() {
        MixinModel mixin1 = new MixinModel(SCHEMA, "m1", SCHEMA + "m2");
        classificationModel.addMixin(mixin1);
        MixinModel mixin2 = new MixinModel(SCHEMA, "m2", SCHEMA + "m1");
        classificationModel.addMixin(mixin2);
        MixinModel mixin3 = new MixinModel(SCHEMA, "m3", SCHEMA + "m3");
        classificationModel.addMixin(mixin3);
        MixinModel mixin4 = new MixinModel(SCHEMA, "m4", null);
        classificationModel.addMixin(mixin4);
        MixinModel mixin5 = new MixinModel(SCHEMA, "m5", SCHEMA + "m9");
        classificationModel.addMixin(mixin5);

        OcciRepresentationModel representationModel = RepresentationModelBuilder.build(classificationModel);
        Assert.assertNull(representationModel.getKind());
        Assert.assertTrue(representationModel.getLinkDefinitions().isEmpty());
        Assert.assertTrue(representationModel.getMixins().isEmpty());
    }

    @Test
    public void complexRepresentation() {
        KindModel kind = new KindModel(SCHEMA, "k");
        classificationModel.setKind(kind);
        LinkModel link1 = new LinkModel(SCHEMA, "l1", null);
        classificationModel.addLink(link1);
        LinkModel link2 = new LinkModel(SCHEMA, "l2", null);
        classificationModel.addLink(link2);
        // category mixins
        MixinModel mixinC1 = new MixinModel(SCHEMA, "c1", Category.CategorySchema + Category.CategoryTerm);
        classificationModel.addMixin(mixinC1);
        MixinModel mixinC11 = new MixinModel(SCHEMA, "c11", mixinC1.getId());
        classificationModel.addMixin(mixinC11);
        MixinModel mixinC111 = new MixinModel(SCHEMA, "c111", mixinC11.getId());
        classificationModel.addMixin(mixinC111);
        MixinModel mixinC12 = new MixinModel(SCHEMA, "c12", mixinC1.getId());
        classificationModel.addMixin(mixinC12);
        MixinModel mixinC2 = new MixinModel(SCHEMA, "c2", Category.CategorySchema + Category.CategoryTerm);
        classificationModel.addMixin(mixinC2);
        // kind mixins
        MixinModel mixinK1 = new MixinModel(SCHEMA, "mk1", kind.getId());
        classificationModel.addMixin(mixinK1);
        MixinModel mixinK11 = new MixinModel(SCHEMA, "mk11", mixinK1.getId());
        classificationModel.addMixin(mixinK11);
        MixinModel mixinK2 = new MixinModel(SCHEMA, "mk2", kind.getId());
        classificationModel.addMixin(mixinK2);
        // link mixins
        MixinModel mixinL11 = new MixinModel(SCHEMA, "ml11", link1.getId());
        classificationModel.addMixin(mixinL11);
        MixinModel mixinL111 = new MixinModel(SCHEMA, "ml111", mixinL11.getId());
        classificationModel.addMixin(mixinL111);
        MixinModel mixinL1111 = new MixinModel(SCHEMA, "ml1111", mixinL111.getId());
        classificationModel.addMixin(mixinL1111);
        MixinModel mixinL12 = new MixinModel(SCHEMA, "ml12", link1.getId());
        classificationModel.addMixin(mixinL12);
        // not apply-able mixins
        MixinModel mixinNull = new MixinModel(SCHEMA, "mNull", null);
        classificationModel.addMixin(mixinNull);
        MixinModel mixinL3 = new MixinModel(SCHEMA, "ml3", SCHEMA + "l3");
        classificationModel.addMixin(mixinL3);
        MixinModel mixinFoo = new MixinModel(SCHEMA, "mFoo", SCHEMA + "mFoo");
        classificationModel.addMixin(mixinFoo);

        OcciRepresentationModel representationModel = RepresentationModelBuilder.build(classificationModel);
        // kind
        Assert.assertEquals(kind, representationModel.getKind());
        // mixins
        Assert.assertEquals(8, representationModel.getMixins().size());
        Assert.assertTrue(representationModel.getMixins().containsAll(
                Arrays.asList(mixinC1, mixinC11, mixinC111, mixinC12, mixinC2, mixinK1, mixinK11, mixinK2)));
        // links
        Assert.assertEquals(2, representationModel.getLinkDefinitions().size());
        Assert.assertTrue(representationModel.getLinkDefinitions().containsAll(Arrays.asList(link1, link2)));
        Assert.assertEquals(9, link1.getMixins().size());
        Assert.assertTrue(link1.getMixins().containsAll(
                Arrays.asList(mixinC1, mixinC11, mixinC111, mixinC12, mixinC2, mixinL11, mixinL111, mixinL1111, mixinL12)));
        Assert.assertEquals(5, link2.getMixins().size());
        Assert.assertTrue(link2.getMixins().containsAll(
                Arrays.asList(mixinC1, mixinC11,mixinC111, mixinC12, mixinC2)));
    }
}
