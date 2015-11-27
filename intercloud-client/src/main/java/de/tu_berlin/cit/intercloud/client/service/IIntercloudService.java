package de.tu_berlin.cit.intercloud.client.service;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IIntercloudService {
    IIntercloudClient newIntercloudClient(String entity, String path) throws URISyntaxException, XMPPException, IOException, SmackException;
    IIntercloudClient getIntercloudClient(String entity, String path) throws URISyntaxException, XMPPException, IOException, SmackException;
}
