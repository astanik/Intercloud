package de.tu_berlin.cit.intercloud.client.service.mock;

import de.tu_berlin.cit.rwx4j.XmppURI;

/**
 * This Plugin can be used to create a Response Representation
 * for a specific media type.
 * It is <b>ONLY</b> used for testing.
 */
public interface IMockResponsePlugin {
    /**
     * @return The media type for which this Plugin creates a Response Representation.
     */
    String getMediaType();

    /**
     * Creates an Response Representation for a specified path of an {@link XmppURI}.
     * @param xwadl
     * @return
     */
    String getRepresentationString(XmppURI xwadl);
}
