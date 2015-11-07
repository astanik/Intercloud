package de.tu_berlin.cit.intercloud.webapp.xmpp;

import de.tu_berlin.cit.intercloud.xmpp.core.packet.JID;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

    public AbstractXMPPConnection getConnection(String jabberId, String password) {
        JID jid = new JID(jabberId);
        XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(jid.getNode(), password)
                .setServiceName(jid.getDomain())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setDebuggerEnabled(true)
                .build();

        return new XMPPTCPConnection(configuration);
    }
}
