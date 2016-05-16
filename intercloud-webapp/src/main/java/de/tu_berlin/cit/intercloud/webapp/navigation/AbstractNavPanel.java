package de.tu_berlin.cit.intercloud.webapp.navigation;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNavPanel extends Panel {
    private final List<NavItem> navItems = new ArrayList<>();

    public AbstractNavPanel(String id, String listViewId) {
        super(id);

        add(new ListView<NavItem>(listViewId, navItems) {
            @Override
            protected void populateItem(ListItem<NavItem> listItem) {
                populateNavItem(listItem);
            }
        });
    }

    protected abstract void populateNavItem(ListItem<NavItem> listItem);

    public void addNavItem(NavItem navItem) {
        navItems.add(navItem);
    }

    protected void setActive(Component c, Class<? extends Page> page) {
        if (this.getPage().getClass().equals(page)) {
            c.add(new AttributeAppender("class", Model.of(" active")));
        }
    }

    protected BookmarkablePageLink<Page> newNavLink(String id, NavItem navItem) {
        BookmarkablePageLink<Page> link = new BookmarkablePageLink<>(id, navItem.getPage());
        link.setBody(Model.of(navItem.getLabel()));
        return link;
    }
}
