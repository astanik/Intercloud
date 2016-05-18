package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.webapp.components.Alert;
import de.tu_berlin.cit.intercloud.webapp.pages.DiscoverPage;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

public class LoginPanel extends Panel {
    private Alert alert;

    public LoginPanel(String id) {
        super(id);

        alert = new Alert("loginAlert", Model.of());
        add(alert);

        add(new LoginForm("loginForm"));
    }

    public class LoginForm extends Form {
        private String username;
        private String password;

        public LoginForm(String markupId) {
            super(markupId);
            this.setDefaultModel(new CompoundPropertyModel<Object>(this));

            this.add(new TextField("username").setRequired(true));
            this.add(new PasswordTextField("password").setRequired(true));

            Button submitBtn = new Button("loginBtn", Model.of("Sign In")) {
                @Override
                public void onSubmit() {
                    if (AuthenticatedWebSession.get().signIn(username, password))
                        setResponsePage(DiscoverPage.class);
                    else {
                        LoginPanel.this.alert.withWarning(Model.of("Could not log in."), Model.of());
                    }
                }
            };
            this.add(submitBtn);
            this.setDefaultButton(submitBtn);
        }

    }
}
