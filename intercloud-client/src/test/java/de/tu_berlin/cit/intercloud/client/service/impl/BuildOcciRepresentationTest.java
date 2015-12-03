package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.OcciRepresentationModel;
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
    private static final String SCHEMA = "example.schema.de";

    IntercloudClient intercloudClient;

    @Before
    public void before() {
        intercloudClient = new IntercloudClient(null, null, null);
    }

    @After
    public void after() {
        intercloudClient = null;
    }

    private <T extends CategoryModel> Map<String, T> newCategoryModelMap(T... categoryModels) {
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
        OcciRepresentationModel representationModel = intercloudClient.buildOcciRepresentationModel(null, new HashMap<>(), new HashMap<>());
        Assert.assertNull(representationModel.getKind());
        Assert.assertTrue(representationModel.getLinks().isEmpty());
        Assert.assertTrue(representationModel.getMixins().isEmpty());
    }

    @Test
    public void noMixins() {
        KindModel kind = new KindModel("k", SCHEMA);
        LinkModel link1 = new LinkModel("l1", SCHEMA, null);
        LinkModel link2 = new LinkModel("l2", SCHEMA, null);

        OcciRepresentationModel representationModel = intercloudClient.buildOcciRepresentationModel(kind, newCategoryModelMap(link1, link2), new HashMap<>());
        Assert.assertEquals(kind, representationModel.getKind());
        Assert.assertEquals(2, representationModel.getLinks().size());
        Assert.assertTrue(representationModel.getLinks().contains(link1));
        Assert.assertTrue(representationModel.getLinks().contains(link2));
    }


    @Test
    public void kindWithMixins() {
        KindModel kind = new KindModel("k", SCHEMA);
        MixinModel mixin1 = new MixinModel("m1", SCHEMA, kind.getId());
        MixinModel mixin2 = new MixinModel("m2", SCHEMA, kind.getId());

        OcciRepresentationModel representationModel = intercloudClient.buildOcciRepresentationModel(kind, new HashMap<>(), newCategoryModelMap(mixin1, mixin2));
        Assert.assertEquals(kind, representationModel.getKind());
        Assert.assertTrue(representationModel.getLinks().isEmpty());
        Assert.assertEquals(2, representationModel.getMixins().size());
        Assert.assertTrue(representationModel.getMixins().contains(mixin1));
        Assert.assertTrue(representationModel.getMixins().contains(mixin2));
    }


    @Test
    public void kindWithMixinsWithMixins() {
        KindModel kind = new KindModel("k", SCHEMA);
        MixinModel mixin1 = new MixinModel("m1", SCHEMA, kind.getId());
        MixinModel mixin11 = new MixinModel("m11", SCHEMA, mixin1.getId());
        MixinModel mixin12 = new MixinModel("m12", SCHEMA, mixin1.getId());
        MixinModel mixin121 = new MixinModel("m121", SCHEMA, mixin12.getId());

        MixinModel mixin2 = new MixinModel("m2", SCHEMA, kind.getId());
        MixinModel mixin21 = new MixinModel("m21", SCHEMA, mixin2.getId());
        MixinModel mixin211 = new MixinModel("m211", SCHEMA, mixin21.getId());

        List<MixinModel> mixinList = Arrays.asList(mixin1, mixin11, mixin12, mixin121, mixin2, mixin21, mixin211);

        OcciRepresentationModel representationModel = intercloudClient.buildOcciRepresentationModel(kind, new HashMap<>(), newCategoryModelMap(mixinList));
        Assert.assertEquals(kind, representationModel.getKind());
        Assert.assertTrue(representationModel.getLinks().isEmpty());
        Assert.assertEquals(7, representationModel.getMixins().size());
        Assert.assertTrue(representationModel.getMixins().containsAll(mixinList));
    }

    @Test
    public void mixinWithMixins() {
        MixinModel mixin1 = new MixinModel("m1", SCHEMA, Category.CategorySchema + Category.CategoryTerm);
        MixinModel mixin11 = new MixinModel("m11", SCHEMA, mixin1.getId());
        MixinModel mixin111 = new MixinModel("m111", SCHEMA, mixin11.getId());
        MixinModel mixin1111 = new MixinModel("m1111", SCHEMA, mixin111.getId());
        MixinModel mixin12 = new MixinModel("m12", SCHEMA, mixin1.getId());

        List<MixinModel> mixinList = Arrays.asList(mixin1, mixin11, mixin111, mixin1111, mixin12);
        OcciRepresentationModel representationModel = intercloudClient.buildOcciRepresentationModel(null, new HashMap<>(), newCategoryModelMap(mixinList));
        Assert.assertNull(representationModel.getKind());
        Assert.assertTrue(representationModel.getLinks().isEmpty());
        Assert.assertEquals(5, representationModel.getMixins().size());
        Assert.assertTrue(representationModel.getMixins().containsAll(mixinList));
    }

    @Test
    public void linksWithMixins() {
        LinkModel link1 = new LinkModel("l1", SCHEMA, null);
        MixinModel mixin11 = new MixinModel("m11", SCHEMA, link1.getId());
        MixinModel mixin111 = new MixinModel("m111", SCHEMA, mixin11.getId());
        MixinModel mixin112 = new MixinModel("m112", SCHEMA, mixin11.getId());
        MixinModel mixin12 = new MixinModel("m12", SCHEMA, link1.getId());

        LinkModel link2 = new LinkModel("l2", SCHEMA, null);
        MixinModel mixin21 = new MixinModel("m21", SCHEMA, link2.getId());

        OcciRepresentationModel representationModel = intercloudClient.buildOcciRepresentationModel(null, newCategoryModelMap(link1, link2),
                newCategoryModelMap(mixin11, mixin111, mixin112, mixin12, mixin21));
        Assert.assertNull(representationModel.getKind());
        Assert.assertTrue(representationModel.getMixins().isEmpty());
        Assert.assertEquals(2, representationModel.getLinks().size());
        Assert.assertTrue(representationModel.getLinks().contains(link1));
        Assert.assertTrue(representationModel.getLinks().contains(link2));
        Assert.assertEquals(4, link1.getMixins().size());
        Assert.assertTrue(link1.getMixins().containsAll(Arrays.asList(mixin11, mixin111, mixin112, mixin12)));
        Assert.assertEquals(1, link2.getMixins().size());
        Assert.assertTrue(link2.getMixins().contains(mixin21));
    }

}
