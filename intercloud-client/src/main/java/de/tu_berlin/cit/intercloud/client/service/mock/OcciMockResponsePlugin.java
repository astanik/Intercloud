package de.tu_berlin.cit.intercloud.client.service.mock;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.occi.OcciRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class OcciMockResponsePlugin implements IMockResponsePlugin {
    private static final Logger logger = LoggerFactory.getLogger(OcciMockResponsePlugin.class);
    private static final Random RANDOM = new Random();
    private final OcciRepresentationModelPlugin occiPlugin = new OcciRepresentationModelPlugin();

    @Override
    public String getMediaType() {
        return OcciXml.MEDIA_TYPE;
    }

    @Override
    public String getRepresentationString(XmppURI xwadl) {
        try {
            File file = new File(xwadl.getPath());
            ResourceTypeDocument resourceTypeDocument = ResourceTypeDocument.Factory.parse(file);
            ResourceTypeDocument.ResourceType resourceType = resourceTypeDocument.getResourceType();
            OcciRepresentationModel representationModel = occiPlugin.getRequestModel(null, resourceType.getGrammars());
            addLinks(representationModel);
            setRandomAttributes(representationModel, xwadl);
            return occiPlugin.getRepresentationString(representationModel);
        } catch (Exception e) {
            logger.error("Could not create sample OCCI response representation.", e);
            return null;
        }
    }

    private void addLinks(OcciRepresentationModel representationModel) {
        for (LinkModel link : representationModel.getLinkDefinitions()) {
            representationModel.addToLinkList(link);
        }
    }

    private void setRandomAttributes(OcciRepresentationModel representationModel, XmppURI xwadl) {
        if (null != representationModel.getKind()) {
            setRandomAttributes(representationModel.getKind());
        }
        setRandomAttributes(representationModel.getMixins());

        for (LinkModel link : representationModel.getLinks()) {
            link.setTarget(xwadl.toString());
            setRandomAttributes(link);
            setRandomAttributes(link.getMixins());
        }
    }

    private void setRandomAttributes(Collection<? extends CategoryModel> categories) {
        categories.forEach(this::setRandomAttributes);
    }

    private void setRandomAttributes(CategoryModel category) {
        category.getAttributes().forEach(this::setRandomAttribute);
    }

    private void setRandomAttribute(AttributeModel attribute) {
        if (!attribute.hasValue()) {
            switch (attribute.getType()) {
                case STRING:
                    attribute.setString(UUID.randomUUID().toString());
                    break;
                case ENUM:
                    attribute.setEnum(UUID.randomUUID().toString());
                    break;
                case INTEGER:
                    attribute.setInteger(RANDOM.nextInt());
                    break;
                case DOUBLE:
                    attribute.setDouble(RANDOM.nextDouble());
                    break;
                case FLOAT:
                    attribute.setFloat(RANDOM.nextFloat());
                    break;
                case BOOLEAN:
                    attribute.setBoolean(RANDOM.nextBoolean());
                    break;
                case URI:
                    attribute.setUri(UUID.randomUUID().toString());
                    break;
                case SIGNATURE:
                    attribute.setSignature(UUID.randomUUID().toString());
                    break;
                case KEY:
                    attribute.setKey(UUID.randomUUID().toString());
                    break;
                case DATETIME:
                    attribute.setDatetime(new Date(System.currentTimeMillis() + RANDOM.nextInt()));
                    break;
                case DURATION:
                    attribute.setDuration(Duration.ofMillis(System.currentTimeMillis() + RANDOM.nextInt()));
                    break;
                case LIST:
                    break;
                case MAP:
                    break;
            }
        }
    }
}
