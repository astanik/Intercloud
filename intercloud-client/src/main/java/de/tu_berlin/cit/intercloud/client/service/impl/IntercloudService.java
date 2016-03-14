package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudService;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public class IntercloudService implements IIntercloudService {
    private final IXmppService xmppService;
    // store latest intercloud client
    // may produce more xmpp requests, but no memory leaks
    // compared to map approach
    private IIntercloudClient intercloudClient;
    private XmppURI intercloudClientUri;

    public IntercloudService(IXmppService xmppService) {
        this.xmppService = xmppService;
    }

    public synchronized IIntercloudClient newIntercloudClient(XmppURI uri) throws XMPPException, IOException, SmackException {
        ResourceTypeDocument xwadl = this.xmppService.receiveXwadlDocument(uri);
        this.intercloudClient = new IntercloudClient(this.xmppService, xwadl, uri);
        this.intercloudClientUri = uri;
        return intercloudClient;
    }

    @Override
    public synchronized IIntercloudClient getIntercloudClient(XmppURI uri) throws XMPPException, IOException, SmackException {
        if (null == this.intercloudClientUri || !this.intercloudClientUri.equals(uri)) {
            return newIntercloudClient(uri);
        }
        return this.intercloudClient;
    }
}
