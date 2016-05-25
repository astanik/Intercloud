package de.tu_berlin.cit.intercloud.client.service.mock;

import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.representations.UriText;

import java.util.UUID;

public class UriMockResponsePlugin implements IMockResponsePlugin {
    @Override
    public String getMediaType() {
        return UriText.MEDIA_TYPE;
    }

    @Override
    public String getRepresentationString(XmppURI xwadl) {
        return xwadl.toString() + "/" + UUID.randomUUID();
    }
}
