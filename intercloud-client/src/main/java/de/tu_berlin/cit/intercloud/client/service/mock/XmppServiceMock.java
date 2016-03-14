package de.tu_berlin.cit.intercloud.client.service.mock;

import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResponseDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.DocumentationType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This mock should <b>ONLY</b> be used for test purpose.
 * It can be used when no xmpp connection or components are up and running.
 * <p>
 * It creates static data depending on the input parameters.
 */
public class XmppServiceMock implements IXmppService {
    private static final Logger logger = LoggerFactory.getLogger(XmppServiceMock.class);
    public static final Map<String, IMockResponsePlugin> RESPONSE_PLUGIN_REGISTRY;
    static {
        RESPONSE_PLUGIN_REGISTRY = new HashMap<>();
        IMockResponsePlugin plugin = new UriListMockResponsePlugin();
        RESPONSE_PLUGIN_REGISTRY.put(plugin.getMediaType(), plugin);
        plugin = new OcciMockResponsePlugin();
        RESPONSE_PLUGIN_REGISTRY.put(plugin.getMediaType(), plugin);
    }

    @Override
    public void connect(XmppURI uri, String password) throws XMPPException, IOException, SmackException {
        // do nothing, accept any credentials
    }

    @Override
    public void disconnect() {
        // do nothing
    }

    /**
     * Creates a static list of dummy items.
     */
    @Override
    public List<XmppURI> discoverRestfulItems(XmppURI uri) throws XMPPException, IOException, SmackException {
        try {
            String userHome = new File(System.getProperty("user.home")).getAbsolutePath();
            return Arrays.asList(new XmppURI("example.component.de", userHome),
                    new XmppURI("exmaple.component.edu", userHome),
                    new XmppURI("example.component.com", userHome));
        } catch (URISyntaxException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Returns a static representation depending on the response media type.
     * <p>
     * text/uri-list: if path is a directory, return list of all directory items
     * text/occi: returns a representation according to the classification of the xwadl's grammars section
     *              with attributes randomly set or default values if given
     */
    @Override
    public ResourceDocument sendRestDocument(XmppURI uri, ResourceDocument document) throws XMPPException, IOException, SmackException {
        ResourceDocument result = (ResourceDocument) document.copy();
        if (null != result.getResource()) {
            MethodDocument.Method method = result.getResource().getMethod();
            if (method.isSetRequest()) {
                method.unsetRequest();
            }
            ResponseDocument.Response response = method.getResponse();
            if (null != response) {
                IMockResponsePlugin plugin = RESPONSE_PLUGIN_REGISTRY.get(response.getMediaType());
                if (null != plugin) {
                    response.setRepresentation(plugin.getRepresentationString(uri));
                }
            }
        }
        return result;
    }

    /**
     * Reads a xml file from within the user's home directory.
     * The file location is taken from the xmpp uri path.
     */
    @Override
    public ResourceTypeDocument receiveXwadlDocument(XmppURI uri) throws XMPPException, IOException, SmackException {
        long timeMillis = System.currentTimeMillis();
        File file = new File(uri.getPath());
        try {
            if (file.isDirectory()) {
                return getDirectoryXwadl(file);
            } else {
                return ResourceTypeDocument.Factory.parse(file);
            }
        } catch (XmlException e) {
            throw new SmackException("Failed to parse resource type document. file: " + file, e);
        } finally {
            logger.info("Xml --> XmlBean: {} ms", System.currentTimeMillis() - timeMillis);
        }
    }

    private ResourceTypeDocument getDirectoryXwadl(File directory) {
        ResourceTypeDocument resourceTypeDocument = ResourceTypeDocument.Factory.newInstance();
        ResourceTypeDocument.ResourceType resourceType = resourceTypeDocument.addNewResourceType();
        resourceType.setPath(directory.getPath());
        de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method method = resourceType.addNewMethod();
        method.setType(MethodType.GET);
        DocumentationType documentationType = method.addNewDocumentation();
        documentationType.setStringValue("Get list of files contained in this folder resource.");
        de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResponseDocument.Response response = method.addNewResponse();
        response.setMediaType(UriListText.MEDIA_TYPE);
        return resourceTypeDocument;
    }
}
