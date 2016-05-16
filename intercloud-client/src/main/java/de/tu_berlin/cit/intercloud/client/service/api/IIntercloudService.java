package de.tu_berlin.cit.intercloud.client.service.api;

import de.tu_berlin.cit.rwx4j.XmppURI;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * This interface is used to request and receive an {@link de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument}
 * based on which it creates an {@link IIntercloudClient}.
 */
public interface IIntercloudService {
    /**
     * Requests and receives an {@link de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument}
     * for a specified {@link XmppURI} and creates a new {@link IIntercloudClient}.
     * Stores the created {@link IIntercloudClient} for later usage.
     * @param uri The URI specifying the xmpp entity and the path where to receive the XWADL from.
     * @return A new Intercloud Client.
     * @throws XMPPException
     * @throws IOException
     * @throws SmackException
     */
    IIntercloudClient newIntercloudClient(XmppURI uri) throws XMPPException, IOException, SmackException;

    /**
     * If the implementation of this class contains an {@link IIntercloudClient} for the
     * specified {@link XmppURI}, the existing client is returned.
     * If it does not contain an {@link IIntercloudClient}, an {@link de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument}
     * is received and a new client is returned.
     * @param uri
     * @return Whether an existing client or a new one.
     * @throws XMPPException
     * @throws IOException
     * @throws SmackException
     */
    IIntercloudClient getIntercloudClient(XmppURI uri) throws XMPPException, IOException, SmackException;
}
