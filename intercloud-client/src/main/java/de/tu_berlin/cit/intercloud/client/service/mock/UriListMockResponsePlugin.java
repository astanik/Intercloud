package de.tu_berlin.cit.intercloud.client.service.mock;

import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.representations.UriListText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UriListMockResponsePlugin implements IMockResponsePlugin {
    private static final Logger logger = LoggerFactory.getLogger(UriListMockResponsePlugin.class);

    @Override
    public String getMediaType() {
        return UriListText.MEDIA_TYPE;
    }

    /**
     * The path of the {@link XmppURI} specifies a file that if:
     * <ul>
     *     <li>it is a directory, returns the a list of files contained in this directory</li>
     *     <li>it is not a directory, returns a static dummy list.</li>
     * </ul>
     * @param xwadl
     * @return
     */
    @Override
    public String getRepresentationString(XmppURI xwadl) {
        File file = new File(xwadl.getPath());
        if (file.isDirectory()) {
            return getDirectoryList(xwadl.getJID(), file);
        } else {
            List<String> uriList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                uriList.add(xwadl.toString() + "/" + UUID.randomUUID());
            }
            return String.join(";", uriList);
        }
    }

    private String getDirectoryList(String jid, File directory) {
        String[] list = directory.list();
        if (null != list) {
            List<String> directoryList = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                try {
                    directoryList.add(new XmppURI(jid, new File(directory, list[i]).getPath()).toString());
                } catch (URISyntaxException e) {
                    logger.error("Could not add file to directory list.", e);
                }
            }
            return String.join(";", directoryList);
        } else {
            return "";
        }
    }
}
