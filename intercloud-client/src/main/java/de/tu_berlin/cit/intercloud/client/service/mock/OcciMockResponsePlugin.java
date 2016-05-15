package de.tu_berlin.cit.intercloud.client.service.mock;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.OcciRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument;
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
    public String getRepresentationString(XmppURI xwadlUri) {
        try {
            File file = new File(xwadlUri.getPath());
            XwadlDocument xwadlDocument = XwadlDocument.Factory.parse(file);
            XwadlDocument.Xwadl xwadl = xwadlDocument.getXwadl();
            OcciRepresentationModel representationModel = occiPlugin.getRequestModel(null, xwadl.getGrammars());
            addLinks(representationModel);
            setRandomAttributes(representationModel, xwadlUri);
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
