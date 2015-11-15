package de.tu_berlin.cit.intercloud.webapp.xmpp;

import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<String> discoverXmppRestfulItems(AbstractXMPPConnection connection, XmppURI uri) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        return discoverItemsByFeature(connection, uri, Arrays.asList(XwadlIQ.NAMESPACE, RestIQ.NAMESPACE));
    }

    private List<String> discoverItemsByFeature(AbstractXMPPConnection connection, XmppURI uri, List<String> features) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        // discover items
        ServiceDiscoveryManager discoveryManager = ServiceDiscoveryManager.getInstanceFor(connection);
        DiscoverItems discoverItems = discoveryManager.discoverItems(uri.getDomain());
        List<DiscoverItems.Item> items = discoverItems.getItems();
        List<String> result = new ArrayList<>();
        // discover infos per item and check if specified feature set is supported
        for (DiscoverItems.Item item : items) {
            DiscoverInfo discoverInfo = discoveryManager.discoverInfo(item.getEntityID());
            boolean conatinsAllFeatures = true;
            for (String feature : features) {
                if (!discoverInfo.containsFeature(feature)) {
                    conatinsAllFeatures = false;
                    break;
                }
            }
            if (conatinsAllFeatures) {
                result.add(item.getEntityID());
            } else if (logger.isDebugEnabled()) {
                logger.debug("Entity {} does not support the specified features.", item.getEntityID());
            }

        }
        return result;
    }
}
