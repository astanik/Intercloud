package de.tu_berlin.cit.intercloud.client.service.mock;

import de.tu_berlin.cit.rwx4j.XmppURI;

public interface IMockResponsePlugin {
    String getMediaType();
    String getRepresentationString(XmppURI xwadl);
}
