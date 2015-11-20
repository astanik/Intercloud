package de.tu_berlin.cit.intercloud.webapp.nav;

import org.apache.wicket.markup.html.list.ListItem;

public class NavStackedPanel extends AbstractNavPanel {
    public NavStackedPanel(String id) {
        super(id, "navItems");
    }

    @Override
    protected void populateNavItem(ListItem<NavItem> listItem) {
        NavItem navItem = listItem.getModelObject();
        listItem.add(newNavLink("navLink", navItem));
        setActive(listItem, navItem.getPage());
    }
}
