package de.tu_berlin.cit.intercloud.webapp.nav;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;

public class NavListGroupPanel extends AbstractNavPanel {
    public NavListGroupPanel(String id) {
        super(id, "navItems");
    }

    @Override
    protected void populateNavItem(ListItem<NavItem> listItem) {
        NavItem navItem = listItem.getModelObject();
        BookmarkablePageLink<Page> link = newNavLink("navLink", navItem);
        setActive(link, navItem.getPage());
        listItem.add(link);
    }
}
