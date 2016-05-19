package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.request.component.IRequestablePage;

/**
 * This interface provides methods to discover XMPP entity supporting REST with XMPP.
 * These methods my be invoked by other Wicket components,
 * that are part of the page implementing this interface.
 */
public interface IDiscoverPage extends IRequestablePage {
    /**
     * Discovers all XMPP entities which are connected to a given XMPP entity
     * and support REST with XPP.
     * @param jid The Jabber ID of the XMPP entity to be discovered
     */
    void discover(String jid);

    /**
     * Connects to a given XMPP entity.
     * @param xmppURI The Jabbber ID and REST path of the XMPP entity to connect to.
     */
    void connect(XmppURI xmppURI);
}
