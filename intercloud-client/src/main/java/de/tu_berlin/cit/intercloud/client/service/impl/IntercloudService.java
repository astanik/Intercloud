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

public class IntercloudService implements IIntercloudService {
    private final IXmppService xmppService;

    public IntercloudService(IXmppService xmppService) {
        this.xmppService = xmppService;
    }

    @Override
    public IIntercloudClient newIntercloudClient(String entity, String path) throws URISyntaxException, XMPPException, IOException, SmackException {
        XmppURI uri = new XmppURI(entity, path);
        ResourceTypeDocument xwadl = xmppService.getXwadlDocument(uri);
        return new IntercloudClient(this.xmppService, xwadl, uri);
    }
}
