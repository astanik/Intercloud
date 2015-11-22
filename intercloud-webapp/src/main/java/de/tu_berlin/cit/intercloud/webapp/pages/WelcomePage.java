package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.webapp.nav.NavItem;
import de.tu_berlin.cit.intercloud.webapp.nav.NavListGroupPanel;
import de.tu_berlin.cit.intercloud.webapp.nav.NavStackedPanel;
import de.tu_berlin.cit.intercloud.webapp.template.Template;

public class WelcomePage extends Template {

    public WelcomePage() {
        super();

        NavStackedPanel navPanel = new NavStackedPanel("navStacked");
        navPanel.addNavItem(new NavItem("Welcome", WelcomePage.class));
        navPanel.addNavItem(new NavItem("Sing In", de.tu_berlin.cit.intercloud.webapp.auth.LoginPage.class));
        navPanel.addNavItem(new NavItem("Login", LoginPage.class));
        navPanel.addNavItem(new NavItem("Examples", ExamplePage.class));
        add(navPanel);

        NavListGroupPanel navList = new NavListGroupPanel("navList");
        navList.addNavItem(new NavItem("Page 1", WelcomePage.class));
        navList.addNavItem(new NavItem("Sing In", de.tu_berlin.cit.intercloud.webapp.auth.LoginPage.class));
        navList.addNavItem(new NavItem("Login", LoginPage.class));
        add(navList);
    }

}
