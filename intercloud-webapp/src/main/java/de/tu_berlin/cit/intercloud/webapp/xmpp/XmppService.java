package de.tu_berlin.cit.intercloud.webapp.xmpp;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class XmppService {
    private final static Logger logger = LoggerFactory.getLogger(XmppService.class);

    private static XmppService instance;

    private XmppService() {

    }

    public static XmppService getInstance() {
        if (null == instance) {
            instance = new XmppService();
        }
        return instance;
    }

    public AbstractXMPPConnection getConnection(String jabberId, String password) throws URISyntaxException {
        return getConnection(new XmppURI(jabberId, ""), password);
    }

    public AbstractXMPPConnection getConnection(XmppURI uri, String password) {
        XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(uri.getNode(), password)
                .setServiceName(uri.getDomain())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setDebuggerEnabled(true)
                .build();

        return new XMPPTCPConnection(configuration);
    }
}
