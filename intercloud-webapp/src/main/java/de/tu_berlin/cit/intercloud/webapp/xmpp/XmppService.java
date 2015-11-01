package de.tu_berlin.cit.intercloud.webapp.xmpp;

import de.tu_berlin.cit.intercloud.util.configuration.ClientConfig;
import de.tu_berlin.cit.intercloud.util.exceptions.ConfigurationException;
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

    public AbstractXMPPConnection connect(XmppUser user) throws IOException, XMPPException, SmackException {
        XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(user.getUsername(), user.getPassword())
                .setServiceName(user.getServiceName())
                .setHost(user.getHost()) // somehow needs to be set
                .setPort(user.getPort())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setDebuggerEnabled(true)
                .build();

        AbstractXMPPConnection connection = new XMPPTCPConnection(configuration);
        connection.connect();
        connection.login();

        return connection;
    }

    public XmppUser generateXmppUser() {
        ClientConfig clientConfig = ClientConfig.getInstance();
        XmppUser user = new XmppUser();

        user.setUsername(clientConfig.getUsername());
        user.setPassword(clientConfig.getPassword());
        user.setServiceName(clientConfig.getServiceName());
        user.setHost(clientConfig.getHost());
        try {
            user.setPort(clientConfig.getPort());
        } catch (ConfigurationException e) {
            logger.error("Failure during xmpp user generation.", e);
        }
        return user;
    }
}
