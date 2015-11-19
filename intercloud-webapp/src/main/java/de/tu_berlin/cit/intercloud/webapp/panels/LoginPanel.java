package de.tu_berlin.cit.intercloud.webapp.panels;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.tu_berlin.cit.intercloud.webapp.ComponentUtils;
import de.tu_berlin.cit.intercloud.webapp.pages.DiscoverItemsPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

public class LoginPanel extends Panel {
    private final Alert alert;

    public LoginPanel(String id) {
        super(id);

        alert = newAlert("loginAlert");
        add(alert);

        add(new LoginForm("loginForm"));
    }

    private Alert newAlert(String markupId) {
        Alert alert = new Alert(markupId, Model.of());
        alert.setOutputMarkupId(true);
        alert.type(Alert.Type.Warning);
        alert.withMessage(Model.of("Could not log in."));
        ComponentUtils.displayNone(alert);
        return alert;
    }

    public class LoginForm extends Form {
        private String username;
        private String password;

        public LoginForm(String markupId) {
            super(markupId);
            this.setDefaultModel(new CompoundPropertyModel<Object>(this));

            this.add(new TextField("username").setRequired(true));
            this.add(new PasswordTextField("password").setRequired(true));

            Button submitBtn = new AjaxButton("loginBtn", Model.of("Sign In")) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    if (AuthenticatedWebSession.get().signIn(username, password))
                        setResponsePage(DiscoverItemsPage.class);
                    else {
                        target.add(ComponentUtils.displayBlock(alert));
                    }
                }
            };
            this.add(submitBtn);
            this.setDefaultButton(submitBtn);
        }

    }
}
