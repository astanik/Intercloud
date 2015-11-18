package de.tu_berlin.cit.intercloud.webapp.nav;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

public class NavStackedPanel extends AbstractNavPanel {
    public NavStackedPanel(String id) {
        super(id, "navItems");
    }

    @Override
    public void addNavItem(NavItem navItem) {
        WebMarkupContainer itemContainer = new WebMarkupContainer(this.navItems.newChildId());
        itemContainer.add(navItem.getLink("navLink"));
        if (navItem.isActive()) {
            itemContainer.add(new AttributeModifier("class", Model.of("active")));
        }
        this.navItems.add(itemContainer);
    }
}
