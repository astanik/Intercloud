package de.tu_berlin.cit.intercloud.xmpp.client.service;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public interface IXmppConnectionManager {
    void connect(XmppURI uri, String password) throws IOException, XMPPException, SmackException;
    AbstractXMPPConnection getConnection() throws SmackException.NotConnectedException;
    void disconnect();
}
