package de.tu_berlin.cit.intercloud.webapp.template;

import de.tu_berlin.cit.intercloud.webapp.nav.NavItem;
import de.tu_berlin.cit.intercloud.webapp.nav.NavListGroupPanel;
import de.tu_berlin.cit.intercloud.webapp.nav.NavStackedPanel;

public class WelcomePage extends Template {

    public WelcomePage() {
        super();

        NavStackedPanel navPanel = new NavStackedPanel("navStacked");
        navPanel.addNavItem(new NavItem("Page 1", WelcomePage.class, false));
        navPanel.addNavItem(new NavItem("Page 2", WelcomePage.class, true));
        navPanel.addNavItem(new NavItem("Page 3", WelcomePage.class, false));
        add(navPanel);

        NavListGroupPanel navList = new NavListGroupPanel("navList");
        navList.addNavItem(new NavItem("Page 1", WelcomePage.class, false));
        navList.addNavItem(new NavItem("Page 2", WelcomePage.class, true));
        navList.addNavItem(new NavItem("Page 3", WelcomePage.class, false));
        add(navList);
    }
}
