package de.tu_berlin.cit.intercloud.xmpp.client.service;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.List;

public interface IXmppService {
    // implementation is using the XmppConnectionManager

    void connect(XmppURI uri, String password) throws XMPPException, IOException, SmackException;
    void disconnect();

    List<XmppURI> discoverRestfulItems(XmppURI uri) throws XMPPException, IOException, SmackException;

    ResourceDocument sendRestDocument(XmppURI uri, ResourceDocument document) throws XMPPException, IOException, SmackException;
    ResourceTypeDocument getXwadlDocument(XmppURI uri) throws XMPPException, IOException, SmackException;
}
