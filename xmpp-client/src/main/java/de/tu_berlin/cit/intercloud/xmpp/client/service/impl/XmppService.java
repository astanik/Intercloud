package de.tu_berlin.cit.intercloud.xmpp.client.service.impl;

import de.tu_berlin.cit.intercloud.xmpp.client.extension.GetXwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppConnectionManager;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XmppService implements IXmppService {
    private static final Logger logger = LoggerFactory.getLogger(XmppService.class);
    private final IXmppConnectionManager connectionManager;

    public XmppService(XmppURI uri, String password) throws XMPPException, IOException, SmackException {
        this.connectionManager = new XmppConnectionManager(uri, password);
    }

    @Override
    public void connect(XmppURI uri, String password) throws XMPPException, IOException, SmackException {
        this.connectionManager.connect(uri, password);
    }

    @Override
    public void disconnect() {
        this.connectionManager.disconnect();
    }

    @Override
    public List<XmppURI> discoverRestfulItems(XmppURI uri) throws XMPPException, IOException, SmackException {
        return discoverItemsByFeature(uri, Arrays.asList(XwadlIQ.NAMESPACE, RestIQ.NAMESPACE));
    }

    private List<XmppURI> discoverItemsByFeature(XmppURI uri, List<String> features) throws XMPPException, IOException, SmackException {
        // discover items
        ServiceDiscoveryManager discoveryManager = ServiceDiscoveryManager.getInstanceFor(this.connectionManager.getConnection());
        DiscoverItems discoverItems = discoveryManager.discoverItems(uri.getDomain());
        List<DiscoverItems.Item> items = discoverItems.getItems();
        List<XmppURI> resultList = new ArrayList<>();
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
                try {
                    resultList.add(getUri(item.getEntityID()));
                } catch (URISyntaxException e) {
                    logger.error("Could not discover item: jid: {}", item.getEntityID(), e);
                }
            } else if (logger.isDebugEnabled()) {
                logger.debug("Entity {} does not support the specified features.", item.getEntityID());
            }
        }

        return resultList;
    }

    private XmppURI getUri(String jid) throws URISyntaxException {
        String restPath = "";
        if (jid.contains("root")) {
            restPath = "/iaas";
        } else if (jid.contains("gateway")) {
            restPath = "/compute";
        }
        return new XmppURI(jid, restPath);
    }

    @Override
    public ResourceDocument sendRestDocument(XmppURI uri, ResourceDocument document) throws XMPPException, IOException, SmackException {
        AbstractXMPPConnection connection = this.connectionManager.getConnection();

        // create an set IQ stanza to uri
        RestIQ setIQ = new RestIQ(uri, document);
        // send stanza
        connection.sendStanza(setIQ);
        // wait for response
        StanzaFilter filter = new AndFilter(new IQReplyFilter(setIQ, connection));
        PacketCollector collector = connection.createPacketCollector(filter);
        IQ resultIQ = collector.nextResultOrThrow();
        if (resultIQ instanceof RestIQ) {
            // create rest doc
            return ((RestIQ) resultIQ).getResourceDocument();
        } else {
            throw new SmackException("Wrong RestIQ has been passed");
        }
    }

    @Override
    public ResourceTypeDocument receiveXwadlDocument(XmppURI uri) throws XMPPException, IOException, SmackException {
        AbstractXMPPConnection connection = this.connectionManager.getConnection();
        // create an get IQ stanza to uri
        IQ getIQ = new GetXwadlIQ(uri);

        // send stanza
        connection.sendStanza(getIQ);
        // wait for response
        StanzaFilter filter = new AndFilter(new IQReplyFilter(getIQ, connection));
        PacketCollector collector = connection.createPacketCollector(filter);
        IQ resultIQ = collector.nextResultOrThrow();
        if (resultIQ instanceof XwadlIQ) {
            // create xwadl
            return ((XwadlIQ) resultIQ).getXwadl();
        } else {
            throw new SmackException("Wrong IQ has been passed");
        }
    }
}
