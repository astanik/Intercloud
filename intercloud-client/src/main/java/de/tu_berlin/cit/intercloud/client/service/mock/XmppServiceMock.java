package de.tu_berlin.cit.intercloud.client.service.mock;

import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.representations.UriListText;
import de.tu_berlin.cit.rwx4j.rest.ActionDocument;
import de.tu_berlin.cit.rwx4j.rest.MethodDocument;
import de.tu_berlin.cit.rwx4j.rest.ResponseDocument;
import de.tu_berlin.cit.rwx4j.rest.RestDocument;
import de.tu_berlin.cit.rwx4j.xwadl.DocumentationType;
import de.tu_berlin.cit.rwx4j.xwadl.MethodType;
import de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument;
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
            return Arrays.asList(new XmppURI("gateway." + uri.getDomain() , userHome),
                    new XmppURI("exchange." + uri.getDomain(), userHome),
                    new XmppURI("root." + uri.getDomain(), userHome));
        } catch (URISyntaxException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Returns a static representation depending on the response media type:
     * <p>
     * text/uri-list: if path is a directory, return list of all directory items
     * <p>
     * text/occi: returns a representation according to the classification of the xwadl's grammars section
     *              with attributes randomly set or default values if given
     * <p>
     * otherwise: an empty representaiton
     */
    @Override
    public RestDocument sendRestDocument(XmppURI uri, RestDocument restDocument) throws XMPPException, IOException, SmackException {
        RestDocument result = (RestDocument) restDocument.copy();
        if (null != result.getRest()) {
            if (result.getRest().isSetMethod()) {
                MethodDocument.Method method = result.getRest().getMethod();
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
            } else if (result.getRest().isSetAction()) {
                ActionDocument.Action action = result.getRest().getAction();
                action.setParameterArray(null);
            }
        }
        return result;
    }

    /**
     * Reads a xml file from the local disc.
     * The file location is taken from the xmpp uri path.
     */
    @Override
    public XwadlDocument receiveXwadlDocument(XmppURI uri) throws XMPPException, IOException, SmackException {
        File file = new File(uri.getPath());
        try {
            if (file.isDirectory()) {
                return getDirectoryXwadl(file);
            } else {
                return XwadlDocument.Factory.parse(file);
            }
        } catch (XmlException e) {
            throw new SmackException("Failed to parse resource type document. file: " + file, e);
        }
    }

    private XwadlDocument getDirectoryXwadl(File directory) {
        XwadlDocument xwadlDocument = XwadlDocument.Factory.newInstance();
        XwadlDocument.Xwadl xwadl = xwadlDocument.addNewXwadl();
        xwadl.setPath(directory.getPath());
        de.tu_berlin.cit.rwx4j.xwadl.MethodDocument.Method method = xwadl.addNewMethod();
        method.setType(MethodType.GET);
        DocumentationType documentationType = method.addNewDocumentation();
        documentationType.setStringValue("Get list of files contained in this folder resource.");
        de.tu_berlin.cit.rwx4j.xwadl.ResponseDocument.Response response = method.addNewResponse();
        response.setMediaType(UriListText.MEDIA_TYPE);
        return xwadlDocument;
    }
}
