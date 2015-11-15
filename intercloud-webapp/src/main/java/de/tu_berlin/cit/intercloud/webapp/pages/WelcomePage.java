package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.webapp.nav.NavItem;
import de.tu_berlin.cit.intercloud.webapp.nav.NavListGroupPanel;
import de.tu_berlin.cit.intercloud.webapp.nav.NavStackedPanel;
import de.tu_berlin.cit.intercloud.webapp.template.Template;
import org.apache.wicket.Page;

public class WelcomePage extends Template {

    public WelcomePage() {
        super();

        NavStackedPanel navPanel = new NavStackedPanel("navStacked");
        navPanel.addNavItem(newNavItem("Page 1", WelcomePage.class));
        navPanel.addNavItem(newNavItem("Page 2", WelcomePage.class));
        navPanel.addNavItem(newNavItem("Page 3", WelcomePage.class));
        add(navPanel);

        NavListGroupPanel navList = new NavListGroupPanel("navList");
        navList.addNavItem(newNavItem("Page 1", WelcomePage.class));
        navList.addNavItem(newNavItem("Sing In", de.tu_berlin.cit.intercloud.webapp.auth.LoginPage.class));
        navList.addNavItem(newNavItem("Login", LoginPage.class));
        add(navList);
    }

    private NavItem newNavItem(String label, Class<? extends Page> page) {
        return new NavItem(label, page, this.getPage().getClass().equals(page));
    }
}
