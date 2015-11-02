package de.tu_berlin.cit.intercloud.webapp.template;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.layout.Index;
import de.tu_berlin.cit.intercloud.webapp.xmpp.XmppService;
import de.tu_berlin.cit.intercloud.webapp.xmpp.XmppUser;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
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

    public LoginPanel(String id) {
        super(id);

        add(new Label("alert"));
        add(new LoginForm(this));
    }

    public class LoginForm extends Form<XmppUser> {
        private XmppUser modelObject;
        private MarkupContainer parent;

        public LoginForm(MarkupContainer parent) {
            super("loginForm", new CompoundPropertyModel(XmppService.getInstance().generateXmppUser()));
            this.modelObject = getModelObject();
            this.parent = parent;

            this.add(new TextField("username", new PropertyModel<String>(modelObject, "username")).setRequired(true));
            this.add(new PasswordTextField("password", new PropertyModel(modelObject, "password")).setRequired(true));
            this.add(new TextField("serviceName", new PropertyModel<String>(modelObject, "serviceName")).setRequired(true));
            this.add(new TextField("host", new PropertyModel<String>(modelObject, "host")).setRequired(true));
            this.add(new NumberTextField("port", new PropertyModel<Integer>(modelObject, "port")).setRequired(true));
        }

        @Override
        protected void onSubmit() {
            XmppUser modelObject = this.getModelObject();
            IntercloudWebSession session = (IntercloudWebSession) this.getSession();

            try {
                AbstractXMPPConnection connection = XmppService.getInstance().connect(modelObject);
                session.authenticate(modelObject, connection);
            } catch (Exception e) {
                error("Failed to connect to XMPP server.");
                logger.error("Failed to connect to XMPP server.", e);
                parent.replace(new Alert("alert", Model.of(e.getMessage()), Model.of("Failed!"))
                        .type(Alert.Type.Danger)
                        .setCloseButtonVisible(true)
                        .useInlineHeader(true));
            }

            if (session.isSignedIn()) {
                getRequestCycle().setResponsePage(Index.class);
            }
        }
    }
}
