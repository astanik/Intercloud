package de.tu_berlin.cit.intercloud.client.service;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public interface IIntercloudService {
    IIntercloudClient newIntercloudClient(XmppURI uri) throws XMPPException, IOException, SmackException;
    IIntercloudClient getIntercloudClient(XmppURI uri) throws XMPPException, IOException, SmackException;
}
