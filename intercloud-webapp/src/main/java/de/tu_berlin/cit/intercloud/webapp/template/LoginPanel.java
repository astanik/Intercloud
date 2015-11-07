package de.tu_berlin.cit.intercloud.webapp.template;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.tu_berlin.cit.intercloud.webapp.layout.Index;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
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
        displayNone(alert);
        return alert;
    }

    private <T extends Component> T displayNone(T component) {
        component.add(new AttributeModifier("style", new Model("display:none")));
        return component;
    }

    private <T extends Component> T displayBlock(T component) {
        component.add(new AttributeModifier("style", new Model("display:block")));
        return component;
    }

    public class LoginForm extends Form {
        private String username;
        private String password;

        public LoginForm(String markupId) {
            super(markupId);
            this.setDefaultModel(new CompoundPropertyModel<Object>(this));

            this.add(new TextField("username").setRequired(true));
            this.add(new PasswordTextField("password").setRequired(true));

            this.add(new AjaxButton("loginBtn", Model.of("Sign In")) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    AuthenticatedWebSession session = (AuthenticatedWebSession) this.getSession();
                    if (session.signIn(username, password))
                        setResponsePage(Index.class);
                    else {
                        target.add(displayBlock(alert));
                    }
                }
            });
        }

    }
}
