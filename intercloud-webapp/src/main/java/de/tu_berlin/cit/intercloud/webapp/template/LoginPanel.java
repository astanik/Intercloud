package de.tu_berlin.cit.intercloud.webapp.template;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.layout.Index;
import de.tu_berlin.cit.intercloud.webapp.xmpp.XmppService;
import de.tu_berlin.cit.intercloud.webapp.xmpp.XmppUser;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPanel extends Panel {
    private final static Logger logger = LoggerFactory.getLogger(LoginForm.class);
    private final Alert alert;

    public LoginPanel(String id) {
        super(id);

        add(new LoginForm());

        alert = new Alert("loginAlert", Model.of());
        alert.setOutputMarkupId(true);
        alert.setCloseButtonVisible(true);
        alert.add(new AttributeModifier("style", new Model("display:none")));
        add(alert);
    }

    public class LoginForm extends Form<XmppUser> {
        private XmppUser modelObject;

        public LoginForm() {
            super("loginForm", new CompoundPropertyModel(XmppService.getInstance().generateXmppUser()));
            this.modelObject = getModelObject();

            this.add(new TextField("username", new PropertyModel<String>(modelObject, "username")).setRequired(true));
            this.add(new PasswordTextField("password", new PropertyModel(modelObject, "password")).setRequired(true));
            this.add(new TextField("serviceName", new PropertyModel<String>(modelObject, "serviceName")).setRequired(true));
            this.add(new TextField("host", new PropertyModel<String>(modelObject, "host")).setRequired(true));
            this.add(new NumberTextField("port", new PropertyModel<Integer>(modelObject, "port")).setRequired(true));

            this.add(new AjaxButton("loginBtn", Model.of("Sign In")) {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    IntercloudWebSession session = (IntercloudWebSession) this.getSession();

                    try {
                        AbstractXMPPConnection connection = XmppService.getInstance().connect(modelObject);
                        session.authenticate(modelObject, connection);
                    } catch (Exception e) {
                        error("Failed to connect to XMPP server.");
                        logger.error("Failed to connect to XMPP server.", e);

                        alert.add(new AttributeModifier("style", new Model("display:block")));
                        alert.type(Alert.Type.Danger);
                        alert.withMessage(Model.of(e.getMessage()));
                        alert.withHeader(Model.of("Failed!"));
                        target.add(alert);
                    }

                    if (session.isSignedIn()) {
                        setResponsePage(Index.class);
                    }
                }
            });
        }

    }
}
