package de.tu_berlin.cit.intercloud.xmpp.client.service.mock;

import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResponseDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * This mock should <b>ONLY</b> be used for test purpose.
 * It can be used when no xmpp connection or components are up and running.
 *
 * It creates static data depending on the input parameters.
 */
public class XmppServiceMock implements IXmppService {
    private static final Logger logger = LoggerFactory.getLogger(XmppServiceMock.class);

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
    public List<String> discoverRestfulItems(XmppURI uri) throws XMPPException, IOException, SmackException {
        return Arrays.asList("example.component.de", "exmaple.component.edu", "example.component.com");
    }

    /**
     * Returns a static representation depending on the response media type
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
                if (UriListText.MEDIA_TYPE.equals(response.getMediaType())) {
                    response.setRepresentation("xmpp://example.component.de#/path0;"
                            + "xmpp://example.component.com#/path0/path1;"
                    + "xmpp://example.component.edu#/path0/path1/path2");
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
    public ResourceTypeDocument getXwadlDocument(XmppURI uri) throws XMPPException, IOException, SmackException {
        long timeMillis = System.currentTimeMillis();
        File file = new File(uri.getPath());
        try {
            return ResourceTypeDocument.Factory.parse(file);
        } catch (XmlException e) {
            throw new SmackException("Failed to parse resource type document. file: " + file, e);
        } finally {
            logger.info("Xml --> XmlBean: {} ms", System.currentTimeMillis() - timeMillis);
        }
    }
}
