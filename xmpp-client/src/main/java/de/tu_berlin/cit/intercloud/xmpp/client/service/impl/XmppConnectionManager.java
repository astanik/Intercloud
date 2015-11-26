package de.tu_berlin.cit.intercloud.xmpp.client.service.impl;

import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQProvider;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQProvider;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppConnectionManager;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class XmppConnectionManager implements IXmppConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(XmppConnectionManager.class);

    private AbstractXMPPConnection connection;

    public XmppConnectionManager(XmppURI uri, String password) throws XMPPException, IOException, SmackException {
        // add xmpp rest provider
        ProviderManager.addIQProvider(XwadlIQ.ELEMENT, XwadlIQ.NAMESPACE, new XwadlIQProvider());
        ProviderManager.addIQProvider(RestIQ.ELEMENT, RestIQ.NAMESPACE, new RestIQProvider());
        connect(uri, password);
    }

    @Override
    public void connect(XmppURI uri, String password) throws IOException, XMPPException, SmackException {
        this.disconnect();

        XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(uri.getNode(), password)
                .setServiceName(uri.getDomain())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setDebuggerEnabled(true)
                .build();
        AbstractXMPPConnection connection = new XMPPTCPConnection(configuration);
        connection.connect();
        connection.login();

        this.connection = connection;
    }

    @Override
    public AbstractXMPPConnection getConnection() throws SmackException.NotConnectedException {
        if (null == this.connection) {
            logger.warn("Could not get Xmpp Connection, connection was null.");
            throw new SmackException.NotConnectedException();
        } else if (!this.connection.isConnected()) {
            try {
                // reconnect if ran into timeout
                this.connection.connect();
            } catch (Exception e) {
                this.connection = null;
                logger.error("Could not get Xmpp Connection, failed to reconnect.");
                throw new SmackException.NotConnectedException();
            }
        }
        return this.connection;
    }

    @Override
    public void disconnect() {
        if (null != this.connection) {
            this.connection.disconnect();
            this.connection = null;
        }
    }
}
