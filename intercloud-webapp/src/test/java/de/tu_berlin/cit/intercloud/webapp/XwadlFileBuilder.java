package de.tu_berlin.cit.intercloud.webapp;

import de.tu_berlin.cit.intercloud.occi.core.IntercloudSchemas;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.LinkClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.MixinClassification;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.GrammarsDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class XwadlFileBuilder {
    public static final String XWADL_ROOT = "target/test-xwadl";
    public static final String SCHEMA = IntercloudSchemas.CitSchemaURL + "test#";

    private static final XwadlFileBuilder INSTANCE = new XwadlFileBuilder();

    public static XwadlFileBuilder getInstance() {
        return INSTANCE;
    }

    public String createXwadlFile(boolean hasKind, int numOfKindMixins,
                                  int numOfLinks, int numOfLinkMixins,
                                  int numOfCategoryMixins
    ) throws IOException {
        ResourceTypeDocument resourceTypeDocument = ResourceTypeDocument.Factory.newInstance();
        ResourceTypeDocument.ResourceType resourceType = resourceTypeDocument.addNewResourceType();
        GrammarsDocument.Grammars grammars = resourceType.addNewGrammars();
        grammars.set(createClassifiactin(hasKind, numOfKindMixins, numOfLinks, numOfLinkMixins, numOfCategoryMixins));
        MethodDocument.Method method = resourceType.addNewMethod();
        method.setType(MethodType.POST);
        method.addNewRequest().setMediaType(OcciXml.MEDIA_TYPE);
        method.addNewResponse().setMediaType(UriText.MEDIA_TYPE);

        return createXwadlFile(resourceTypeDocument);
    }

    private String createXwadlFile(ResourceTypeDocument resourceTypeDocument) throws IOException {
        File xwadlRoot = new File(XWADL_ROOT);
        xwadlRoot.mkdirs();
        File tempFile = File.createTempFile("xwadl-", ".xml", xwadlRoot);
        resourceTypeDocument.getResourceType().setPath(tempFile.getPath());
        PrintWriter printWriter = new PrintWriter(tempFile);
        try {
            printWriter.print(resourceTypeDocument.toString());
        } finally {
            printWriter.close();
        }
        return tempFile.getPath();
    }

    private ClassificationDocument createClassifiactin(boolean hasKind, int numOfKindMixins,
                                                       int numOfLinks, int numOfLinkMixins,
                                                       int numOfCategoryMiins
    ) {
        ClassificationDocument classificationDocument = ClassificationDocument.Factory.newInstance();
        ClassificationDocument.Classification classification = classificationDocument.addNewClassification();
        List<MixinClassification> mixinList = new ArrayList<>();
        // Kind
        if (hasKind) {
            classification.setKindType(createKind("my_kind"));
            mixinList.addAll(createMixins("my_kind_mixin_", SCHEMA + "my_kind", numOfKindMixins));
        }
        // Links
        if (0 < numOfLinks) {
            List<LinkClassification> linkList = createLinks("my_link_", numOfLinks);
            classification.setLinkTypeArray(linkList.toArray(new LinkClassification[linkList.size()]));
            mixinList.addAll(createMixins(linkList, numOfLinkMixins));
        }
        // Category Mixins
        if (0 < numOfCategoryMiins) {
            mixinList.addAll(createMixins("my_category_mixin_", Category.CategorySchema + Category.CategoryTerm, numOfCategoryMiins));
        }

        classification.setMixinTypeArray(mixinList.toArray(new MixinClassification[mixinList.size()]));
        return classificationDocument;
    }

    private CategoryClassification createKind(String term) {
        CategoryClassification kind = CategoryClassification.Factory.newInstance();
        kind.setSchema(SCHEMA);
        kind.setTerm(term);
        kind.setTitle(UUID.randomUUID().toString());
        kind.setAttributeClassificationArray(createAttributes());
        return kind;
    }

    private List<LinkClassification> createLinks(String termPrefix, int numberOfLinks) {
        List<LinkClassification> linkList = new ArrayList<>();
        if (0 < numberOfLinks) {
            linkList.add(createLink(termPrefix + 0));

            for (int i = 1; i < numberOfLinks; i++) {
                linkList.add(createLink(termPrefix + i));
            }
        }
        return linkList;
    }

    private LinkClassification createLink(String term) {
        LinkClassification link = LinkClassification.Factory.newInstance();
        link.setSchema(SCHEMA);
        link.setTerm(term);
        link.setTitle(UUID.randomUUID().toString());
        link.setRelation(SCHEMA + "target_term");
        //link.setAttributeClassificationArray(createAttributes());
        return link;
    }

    private List<MixinClassification> createMixins(List<LinkClassification> appliesList, int numberOfMixins) {
        List<MixinClassification> mixinList = new ArrayList<>();
        if (0 < numberOfMixins) {
            for (LinkClassification link : appliesList) {
                mixinList.addAll(createMixins(link.getTerm() + "_mixin_", link.getSchema() + link.getTerm(), numberOfMixins));
            }
        }
        return mixinList;
    }

    private List<MixinClassification> createMixins(String termPrefix, String applies, int numberOfMixins) {
        List<MixinClassification> mixinList = new ArrayList<>();
        if (0 < numberOfMixins) {
            mixinList.add(createMixin(termPrefix + 0, applies));

            for (int i = 1; i < numberOfMixins; i++) {
                mixinList.add(createMixin(termPrefix + i, SCHEMA + termPrefix + (i - 1)));
            }
        }
        return mixinList;
    }

    private MixinClassification createMixin(String term, String applies) {
        MixinClassification mixin = MixinClassification.Factory.newInstance();
        mixin.setSchema(SCHEMA);
        mixin.setTerm(term);
        mixin.setTitle(UUID.randomUUID().toString());
        mixin.setApplies(applies);
        mixin.setAttributeClassificationArray(createAttributes());
        return mixin;
    }

    private AttributeClassificationDocument.AttributeClassification[] createAttributes() {
        List<AttributeClassificationDocument.AttributeClassification> attributeList = new ArrayList<>();
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.STRING, "some string..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.ENUM, "some enum..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.INTEGER, "some integer..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.DOUBLE, "some double..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.FLOAT, "some float..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.BOOLEAN, "some boolean..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.URI, "some uri..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.SIGNATURE, "some signature..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.KEY, "some key..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.DATETIME, "some datetime..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.DURATION, "some duration..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.LIST, "some list..."));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.MAP, "some map..."));
        return attributeList.toArray(new AttributeClassificationDocument.AttributeClassification[attributeList.size()]);
    }

    private AttributeClassificationDocument.AttributeClassification createAttribute(String name,
                                                                                    AttributeType.Enum type,
                                                                                    String description
    ) {
        return createAttribute(name, type, false, true, description, null);
    }

    private AttributeClassificationDocument.AttributeClassification createAttribute(String name,
                                                                                    AttributeType.Enum type,
                                                                                    boolean required,
                                                                                    boolean mutable,
                                                                                    String description,
                                                                                    String xdefault
    ) {
        AttributeClassificationDocument.AttributeClassification attribute = AttributeClassificationDocument.AttributeClassification.Factory.newInstance();
        attribute.setName(name);
        attribute.setType(type);
        attribute.setRequired(required);
        attribute.setMutable(mutable);
        attribute.setDescription(description);
        attribute.setDefault(xdefault);
        return attribute;
    }
}
