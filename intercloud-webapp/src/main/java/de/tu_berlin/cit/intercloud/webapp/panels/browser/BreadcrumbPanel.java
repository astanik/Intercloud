package de.tu_berlin.cit.intercloud.webapp.panels.browser;

import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * The panel provides a breadcrumb navigation
 * (<a href="https://en.wikipedia.org/wiki/Breadcrumb_(navigation)">Wikipedia Breadcrumb</a>)
 * for an {@link XmppURI}.
 * It display each resource path element as a separate link. The link's response page is specified
 * by the {@link IBreadcrumbRedirect}.
 */
public class BreadcrumbPanel extends Panel {
    private static Logger logger = LoggerFactory.getLogger(BreadcrumbPanel.class);

    public BreadcrumbPanel(String id, IModel<XmppURI> uriModel, IBreadcrumbRedirect redirect) {
        super(id);

        IModel<List<Item>> itemList = new LoadableDetachableModel<List<Item>>() {
            @Override
            protected List<Item> load() {
                return getItems(uriModel.getObject());
            }
        };

        this.add(new ListView<Item>("breadcrumb", itemList) {
            @Override
            protected void populateItem(ListItem<Item> listItem) {
                Item item = listItem.getModelObject();

                listItem.add(new Link("link") {
                    @Override
                    public void onClick() {
                        try {
                            XmppURI redirectUri = new XmppURI(uriModel.getObject().getJID(), item.getPath());
                            setResponsePage(redirect.getResponsePage(redirectUri));
                        } catch (URISyntaxException e) {
                            logger.error("Failed to redirect via Breadcrumb.", e);
                        }
                    }
                }.setBody(Model.of(item.getLabel())));
            }
        });
    }

    /**
     * @param uri The XMPP URI providing a specific path.
     * @return A list path elements separated by "/".
     */
    private List<Item> getItems(XmppURI uri) {
        List<Item> itemList = new ArrayList<>();
        if (null != uri.getPath()) {
            String[] paths = uri.getPath().split("/");
            String path = "";
            for (String p : paths) {
                if (!p.trim().isEmpty()) {
                    path += "/" + p;
                    itemList.add(new Item(p, path));
                }
            }
        }
        return itemList;
    }

    /**
     * Represents a resource path element.
     */
    private class Item implements Serializable {
        private static final long serialVersionUID = 67036265165046241L;

        private String label;
        private String path;

        public Item(String label, String path) {
            this.label = label;
            this.path = path;
        }

        public String getLabel() {
            return label;
        }

        public String getPath() {
            return path;
        }
    }
}
