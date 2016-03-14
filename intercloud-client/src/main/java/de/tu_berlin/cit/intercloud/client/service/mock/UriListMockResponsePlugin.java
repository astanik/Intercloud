package de.tu_berlin.cit.intercloud.client.service.mock;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class UriListMockResponsePlugin implements IMockResponsePlugin {
    private static final Logger logger = LoggerFactory.getLogger(UriListMockResponsePlugin.class);

    @Override
    public String getMediaType() {
        return UriListText.MEDIA_TYPE;
    }

    @Override
    public String getRepresentationString(XmppURI xwadl) {
        File file = new File(xwadl.getPath());
        if (file.isDirectory()) {
            return getDirectoryList(xwadl.getJID(), file);
        } else {
            return "xmpp://example.component.de#/path0;"
                    + "xmpp://example.component.com#/path0/path1;"
                    + "xmpp://example.component.edu#/path0/path1/path2";
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
