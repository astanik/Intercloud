package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.webapp.panels.LoginPanel;
import de.tu_berlin.cit.intercloud.webapp.template.Template;

public class LoginPage extends Template {

    public LoginPage() {
        super();

        // add form
        add(new LoginPanel("loginPanel"));
    }
}
