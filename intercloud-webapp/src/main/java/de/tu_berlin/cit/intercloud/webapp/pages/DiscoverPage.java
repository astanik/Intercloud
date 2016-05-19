package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.components.Alert;
import de.tu_berlin.cit.intercloud.webapp.panels.discover.DiscoverPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.discover.DiscoveredItemsPanel;
import de.tu_berlin.cit.intercloud.webapp.template.UserTemplate;
import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This page is used to discover XMPP entities supporting REST with XMPP.
 * It contains the components:
 * <ul>
 * <li>{@link Alert}
 * provides a Bootstrap alert in order to display exceptions or other information.
 * </li>
 * <li>{@link DiscoverPage}
 * to select the XMPP entity that will be discovered.
 * </li>
 * <li>{@link DiscoveredItemsPanel}</li>
 * provides a radio group of discovered items.
 * </ul>
 */
public class DiscoverPage extends UserTemplate implements IDiscoverPage {
    private static final Logger logger = LoggerFactory.getLogger(DiscoverPage.class);

    private IModel<List<XmppURI>> discoItems = new ListModel<>();
    private Alert alert;

    public DiscoverPage() {
        super();

        this.add(new DiscoverPanel("discoPanel", Model.of(IntercloudWebSession.get().getUser().getUri().getDomain()), this));
        this.add(new DiscoveredItemsPanel("discoItemsPanel", discoItems, this));

        alert = new Alert("alert", Model.of());
        this.add(alert);
    }

    @Override
    public void discover(String jid) {
        try {
            XmppURI xmppURI = new XmppURI(jid);
            List<XmppURI> discoItems = IntercloudWebSession.get().getXmppService().discoverRestfulItems(xmppURI);
            this.discoItems.setObject(discoItems);
            if (discoItems.isEmpty()) {
                this.alert.withInfo(Model.of("Cloud not discover any RESTful item for the specified XMPP entity."), Model.of());
            } else {
                this.alert.noMessage();
            }
        } catch (Throwable t) {
            logger.error("Failed to discover item for jid '{}'.", jid, t);
            this.discoItems.setObject(null);
            this.alert.withError(t);
        }
    }

    @Override
    public void connect(XmppURI xmppURI) {
        if (null != xmppURI) {
            this.setResponsePage(new BrowserPage(Model.of(xmppURI)));
        }
    }
}
