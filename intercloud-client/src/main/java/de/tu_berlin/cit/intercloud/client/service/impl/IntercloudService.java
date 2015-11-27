package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.service.IIntercloudService;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class IntercloudService implements IIntercloudService {
    // TODO: clean up map
    private final Map<XmppURI, IIntercloudClient> clientMap = new HashMap<>();
    private final IXmppService xmppService;

    public IntercloudService(IXmppService xmppService) {
        this.xmppService = xmppService;
    }

    @Override
    public synchronized IIntercloudClient newIntercloudClient(String entity, String path) throws URISyntaxException, XMPPException, IOException, SmackException {
        XmppURI uri = new XmppURI(entity, path);
        this.clientMap.remove(uri);

        ResourceTypeDocument xwadl = this.xmppService.getXwadlDocument(uri);
        IntercloudClient intercloudClient = new IntercloudClient(this.xmppService, xwadl, uri);
        clientMap.put(uri, new IntercloudClient(this.xmppService, xwadl, uri));
        return intercloudClient;
    }

    @Override
    public synchronized IIntercloudClient getIntercloudClient(String entity, String path) throws URISyntaxException, XMPPException, IOException, SmackException {
        IIntercloudClient client = this.clientMap.get(new XmppURI(entity, path));
        if (null == client) {
            client = newIntercloudClient(entity, path);
        }
        return client;
    }
}
