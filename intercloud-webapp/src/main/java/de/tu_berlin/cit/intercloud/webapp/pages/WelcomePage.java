package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.webapp.navigation.NavItem;
import de.tu_berlin.cit.intercloud.webapp.navigation.NavListGroupPanel;
import de.tu_berlin.cit.intercloud.webapp.navigation.NavStackedPanel;
import de.tu_berlin.cit.intercloud.webapp.template.Template;

public class WelcomePage extends Template {

    public WelcomePage() {
        super();

        NavStackedPanel navPanel = new NavStackedPanel("navStacked");
        navPanel.addNavItem(new NavItem("Welcome", WelcomePage.class));
        navPanel.addNavItem(new NavItem("Login", LoginPage.class));
        navPanel.addNavItem(new NavItem("Examples", ExamplePage.class));
        add(navPanel);

        NavListGroupPanel navList = new NavListGroupPanel("navList");
        navList.addNavItem(new NavItem("Welcome", WelcomePage.class));
        navList.addNavItem(new NavItem("Login", LoginPage.class));
        navList.addNavItem(new NavItem("Examples", ExamplePage.class));
        add(navList);
    }

}
