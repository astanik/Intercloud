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
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.GrammarsDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.RequestDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class XwadlFileBuilder {
    public static final String XWADL_ROOT = "target/test-xwadl";
    public static final String SCHEMA = IntercloudSchemas.CitSchemaURL + "test#";

    private static final Random RANDOM = new Random();
    private static final XwadlFileBuilder INSTANCE = new XwadlFileBuilder();

    public static XwadlFileBuilder getInstance() {
        return INSTANCE;
    }

    public String createXwadlFile(XwadlFileConfig config) throws IOException {
        ResourceTypeDocument resourceTypeDocument = ResourceTypeDocument.Factory.newInstance();
        ResourceTypeDocument.ResourceType resourceType = resourceTypeDocument.addNewResourceType();
        GrammarsDocument.Grammars grammars = resourceType.addNewGrammars();
        ClassificationDocument classification = createClassification(config);
        grammars.set(classification);
        // METHOD: POST
        MethodDocument.Method method = resourceType.addNewMethod();
        method.setType(MethodType.POST);
        RequestDocument.Request request = method.addNewRequest();
        request.setMediaType(OcciXml.MEDIA_TYPE);
        List<String> templates = createTemplates(classification, config.getNumOfTemplates());
        if (!templates.isEmpty()) {
            request.setTemplateArray(templates.toArray(new String[templates.size()]));
        }
        method.addNewResponse().setMediaType(UriText.MEDIA_TYPE);

        return createXwadlFile(resourceTypeDocument, generateFilename(config));
    }

    private String generateFilename(XwadlFileConfig config) {
        StringBuilder fileName = new StringBuilder();
        if (config.hasKind()) {
            fileName.append("k-");
        }
        if (0 < config.getNumOfKindMixins()) {
            fileName.append(config.getNumOfKindMixins()).append("km-");
        }
        if (0 < config.getNumOfLinks()) {
            fileName.append(config.getNumOfLinks()).append("l-");
        }
        if (0 < config.getNumOfLinkMixins()) {
            fileName.append(config.getNumOfLinkMixins()).append("lm-");
        }
        if (0 < config.getNumOfCategoryMixins()) {
            fileName.append(config.getNumOfCategoryMixins()).append("cm-");
        }
        if (0 < config.getNumOfTemplates()) {
            fileName.append(config.getNumOfTemplates()).append("t-");
        }
        if (config.hasDefaultValues()) {
            fileName.append("d-");
        }
        return fileName.toString();
    }

    private String createXwadlFile(ResourceTypeDocument resourceTypeDocument, String name) throws IOException {
        File xwadlRoot = new File(XWADL_ROOT);
        xwadlRoot.mkdirs();
        File file = new File(xwadlRoot, "xwadl-" + name + System.currentTimeMillis() + ".xml");
        if (file.exists()) {
            file.delete();
        }
        resourceTypeDocument.getResourceType().setPath(file.getPath());
        PrintWriter printWriter = new PrintWriter(file);
        try {
            printWriter.print(resourceTypeDocument.toString());
        } finally {
            printWriter.close();
        }
        return file.getPath();
    }

    private ClassificationDocument createClassification(XwadlFileConfig config) {
        ClassificationDocument classificationDocument = ClassificationDocument.Factory.newInstance();
        ClassificationDocument.Classification classification = classificationDocument.addNewClassification();
        List<MixinClassification> mixinList = new ArrayList<>();
        // Kind
        if (config.hasKind()) {
            classification.setKindType(createKind("my_kind", config.hasDefaultValues()));
            mixinList.addAll(createMixins("my_kind_mixin_", SCHEMA + "my_kind", config.getNumOfKindMixins(), config.hasDefaultValues()));
        }
        // Links
        if (0 < config.getNumOfLinks()) {
            List<LinkClassification> linkList = createLinks("my_link_", config.getNumOfLinks(), config.hasDefaultValues());
            classification.setLinkTypeArray(linkList.toArray(new LinkClassification[linkList.size()]));
            mixinList.addAll(createMixins(linkList, config.getNumOfLinkMixins(), config.hasDefaultValues()));
        }
        // Category Mixins
        if (0 < config.getNumOfCategoryMixins()) {
            mixinList.addAll(createMixins("my_category_mixin_", Category.CategorySchema + Category.CategoryTerm,
                    config.getNumOfCategoryMixins(), config.hasDefaultValues()));
        }

        classification.setMixinTypeArray(mixinList.toArray(new MixinClassification[mixinList.size()]));
        return classificationDocument;
    }

    private CategoryClassification createKind(String term, boolean hasDefaultValues) {
        CategoryClassification kind = CategoryClassification.Factory.newInstance();
        kind.setSchema(SCHEMA);
        kind.setTerm(term);
        kind.setTitle("kind-" + UUID.randomUUID().toString());
        kind.setAttributeClassificationArray(createAttributes(hasDefaultValues));
        return kind;
    }

    private List<LinkClassification> createLinks(String termPrefix, int numberOfLinks, boolean hasDefaultValues) {
        List<LinkClassification> linkList = new ArrayList<>();
        if (0 < numberOfLinks) {
            linkList.add(createLink(termPrefix + 0, hasDefaultValues));

            for (int i = 1; i < numberOfLinks; i++) {
                linkList.add(createLink(termPrefix + i, hasDefaultValues));
            }
        }
        return linkList;
    }

    private LinkClassification createLink(String term, boolean hasDafaultValues) {
        LinkClassification link = LinkClassification.Factory.newInstance();
        link.setSchema(SCHEMA);
        link.setTerm(term);
        link.setTitle("link-" + UUID.randomUUID().toString());
        link.setRelation(SCHEMA + "target_term");
        //link.setAttributeClassificationArray(createAttributes());
        return link;
    }

    private List<MixinClassification> createMixins(List<LinkClassification> appliesList, int numberOfMixins, boolean hasDefaultValues) {
        List<MixinClassification> mixinList = new ArrayList<>();
        if (0 < numberOfMixins) {
            for (LinkClassification link : appliesList) {
                mixinList.addAll(createMixins(link.getTerm() + "_mixin_", link.getSchema() + link.getTerm(), numberOfMixins, hasDefaultValues));
            }
        }
        return mixinList;
    }

    private List<MixinClassification> createMixins(String termPrefix, String applies, int numberOfMixins, boolean hasDefaultValues) {
        List<MixinClassification> mixinList = new ArrayList<>();
        if (0 < numberOfMixins) {
            mixinList.add(createMixin(termPrefix + 0, applies, hasDefaultValues));

            for (int i = 1; i < numberOfMixins; i++) {
                mixinList.add(createMixin(termPrefix + i, SCHEMA + termPrefix + (i - 1), hasDefaultValues));
            }
        }
        return mixinList;
    }

    private MixinClassification createMixin(String term, String applies, boolean hasDefaultValues) {
        MixinClassification mixin = MixinClassification.Factory.newInstance();
        mixin.setSchema(SCHEMA);
        mixin.setTerm(term);
        mixin.setTitle("mixin-" + UUID.randomUUID().toString());
        mixin.setApplies(applies);
        mixin.setAttributeClassificationArray(createAttributes(hasDefaultValues));
        return mixin;
    }

    private AttributeClassificationDocument.AttributeClassification[] createAttributes(boolean hasDefaultValues) {
        List<AttributeClassificationDocument.AttributeClassification> attributeList = new ArrayList<>();
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.STRING, "some string...",
                hasDefaultValues ? UUID.randomUUID().toString() : null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.INTEGER, "some integer...",
                hasDefaultValues ? Integer.toString(RANDOM.nextInt()) : null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.DOUBLE, "some double...",
                hasDefaultValues ? Double.toString(RANDOM.nextDouble()) : null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.FLOAT, "some float...",
                hasDefaultValues ? Float.toString(RANDOM.nextFloat()) : null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.BOOLEAN, "some boolean...",
                hasDefaultValues ? Boolean.toString(RANDOM.nextBoolean()) : null));
        String xdefault = null;
        if (hasDefaultValues) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(RANDOM.nextInt());
            xdefault = DatatypeConverter.printDateTime(calendar);
        }
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.DATETIME, "some datetime...", xdefault));
        // no defaults
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.ENUM, "some enum...", null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.URI, "some uri...", null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.SIGNATURE, "some signature...", null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.KEY, "some key...", null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.DURATION, "some duration...", null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.LIST, "some list...", null));
        attributeList.add(createAttribute(UUID.randomUUID().toString(), AttributeType.MAP, "some map...", null));
        return attributeList.toArray(new AttributeClassificationDocument.AttributeClassification[attributeList.size()]);
    }

    private AttributeClassificationDocument.AttributeClassification createAttribute(String name,
                                                                                    AttributeType.Enum type,
                                                                                    String description,
                                                                                    String xdefault
    ) {
        return createAttribute(name, type, false, true, description, xdefault);
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

    /*
     * TEMPLATE
     */

    private List<String> createTemplates(ClassificationDocument classificationDocument, int numOfTemplates) {
        ClassificationDocument.Classification classification = classificationDocument.getClassification();
        List<String> templateList = new ArrayList<>();
        for (int i = 0; i < numOfTemplates; i++) {
            if (null != classification.getKindType()) {
                templateList.add(createKindTemplate(classification.getKindType()));
            }
            if (null != classification.getMixinTypeArray()) {
                templateList.addAll(createMixinTemplates(classification.getMixinTypeArray()));
            }
            if (null != classification.getLinkTypeArray()) {
                templateList.addAll(createLinkTemplates(classification.getLinkTypeArray()));
            }
        }
        return templateList;
    }

    private String createKindTemplate(CategoryClassification kind) {
        CategoryDocument categoryDocument = CategoryDocument.Factory.newInstance();
        CategoryDocument.Category category = categoryDocument.addNewCategory();
        CategoryType kindTemplate = category.addNewKind();
        kindTemplate.setSchema(kind.getSchema());
        kindTemplate.setTerm(kind.getTerm());
        kindTemplate.setTitle("template-" + UUID.randomUUID().toString());
        if (null != kind.getAttributeClassificationArray()) {
            kindTemplate.setAttributeArray(createAttributeTemplates(kind.getAttributeClassificationArray()));
        }
        return categoryDocument.toString();
    }

    private List<String> createLinkTemplates(LinkClassification[] linkArray) {
        List<String> templateList = new ArrayList<>();
        CategoryDocument categoryDocument;
        CategoryDocument.Category category;
        LinkType linkTemplate;
        for (LinkClassification link : linkArray) {
            categoryDocument = CategoryDocument.Factory.newInstance();
            category = categoryDocument.addNewCategory();
            linkTemplate = category.addNewLink();

            linkTemplate.setSchema(link.getSchema());
            linkTemplate.setTerm(link.getTerm());
            linkTemplate.setTitle("template-" + UUID.randomUUID().toString());
            if (null != link.getAttributeClassificationArray()) {
                linkTemplate.setAttributeArray(createAttributeTemplates(link.getAttributeClassificationArray()));
            }
            linkTemplate.setTarget(UUID.randomUUID().toString());
            templateList.add(categoryDocument.toString());
        }
        return templateList;
    }

    private List<String> createMixinTemplates(MixinClassification[] mixinArray) {
        List<String> templateList = new ArrayList<>();
        CategoryDocument categoryDocument;
        CategoryDocument.Category category;
        CategoryType mixinTemplate;
        for (MixinClassification mixin : mixinArray) {
            categoryDocument = CategoryDocument.Factory.newInstance();
            category = categoryDocument.addNewCategory();
            mixinTemplate = category.addNewMixin();

            mixinTemplate.setSchema(mixin.getSchema());
            mixinTemplate.setTerm(mixin.getTerm());
            mixinTemplate.setTitle("template-" + UUID.randomUUID().toString());
            if (null != mixin.getAttributeClassificationArray()) {
                mixinTemplate.setAttributeArray(createAttributeTemplates(mixin.getAttributeClassificationArray()));
            }
            templateList.add(categoryDocument.toString());
        }
        return templateList;
    }

    private de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType[]
    createAttributeTemplates(AttributeClassificationDocument.AttributeClassification[] attributeArray) {
        List<de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType> templateList =
                new ArrayList<>();
        de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType attributeTemplate;
        Calendar calendar;
        for (AttributeClassificationDocument.AttributeClassification attribute : attributeArray) {
            AttributeType.Enum type = attribute.getType();
            if (AttributeType.STRING.equals(type)) {
                attributeTemplate = de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType.Factory.newInstance();
                attributeTemplate.setName(attribute.getName());
                attributeTemplate.setSTRING(UUID.randomUUID().toString());
                templateList.add(attributeTemplate);
            } else if (AttributeType.INTEGER.equals(type)) {
                attributeTemplate = de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType.Factory.newInstance();
                attributeTemplate.setName(attribute.getName());
                attributeTemplate.setINTEGER(RANDOM.nextInt());
                templateList.add(attributeTemplate);
            } else if (AttributeType.DOUBLE.equals(type)) {
                attributeTemplate = de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType.Factory.newInstance();
                attributeTemplate.setName(attribute.getName());
                attributeTemplate.setDOUBLE(RANDOM.nextDouble());
                templateList.add(attributeTemplate);
            } else if (AttributeType.FLOAT.equals(type)) {
                attributeTemplate = de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType.Factory.newInstance();
                attributeTemplate.setName(attribute.getName());
                attributeTemplate.setFLOAT(RANDOM.nextFloat());
                templateList.add(attributeTemplate);
            } else if (AttributeType.DATETIME.equals(type)) {
                attributeTemplate = de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType.Factory.newInstance();
                attributeTemplate.setName(attribute.getName());
                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(RANDOM.nextInt());
                attributeTemplate.setDATETIME(calendar);
                templateList.add(attributeTemplate);
            } else if (AttributeType.BOOLEAN.equals(type)) {
                attributeTemplate = de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType.Factory.newInstance();
                attributeTemplate.setName(attribute.getName());
                attributeTemplate.setBOOLEAN(RANDOM.nextBoolean());
                templateList.add(attributeTemplate);
            }

        }
        return templateList.toArray(new de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType[templateList.size()]);
    }
}
