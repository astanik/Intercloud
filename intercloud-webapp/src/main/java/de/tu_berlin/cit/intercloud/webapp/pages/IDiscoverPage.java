package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.rwx4j.XmppURI;

public interface IDiscoverPage {
    void discover(String jid);
    void connect(XmppURI xmppURI);
}
