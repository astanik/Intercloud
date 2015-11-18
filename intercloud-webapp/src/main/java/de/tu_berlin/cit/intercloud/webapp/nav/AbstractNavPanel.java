package de.tu_berlin.cit.intercloud.webapp.nav;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

import java.util.List;

public abstract class AbstractNavPanel extends Panel {
    protected final RepeatingView navItems;

    public AbstractNavPanel(String id, String navItemsId) {
        super(id);
        this.navItems = new RepeatingView(navItemsId);
        add(this.navItems);
    }

    public abstract void addNavItem(NavItem navItem);

    public void addNavItems(List<NavItem> navItems) {
        for(NavItem n : navItems) {
            addNavItem(n);
        }
    }
}
