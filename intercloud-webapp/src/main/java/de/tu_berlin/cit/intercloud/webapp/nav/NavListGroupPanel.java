package de.tu_berlin.cit.intercloud.webapp.nav;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;

public class NavListGroupPanel extends AbstractNavPanel {
    public NavListGroupPanel(String id) {
        super(id, "navLinks");
    }

    @Override
    public void addNavItem(NavItem navItem) {
        BookmarkablePageLink<Page> link = navItem.getLink(this.navItems.newChildId());
        if (navItem.isActive()) {
            link.add(new AttributeAppender("class", Model.of(" active")));
        }
        this.navItems.add(link);
    }
}
