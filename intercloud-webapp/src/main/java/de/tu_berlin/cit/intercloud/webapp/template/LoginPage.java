package de.tu_berlin.cit.intercloud.webapp.template;

public class LoginPage extends Template {

    public LoginPage() {
        super();

        // add form
        add(new LoginPanel("loginPanel"));
    }
}
