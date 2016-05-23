package de.tu_berlin.cit.intercloud.webapp.panels.browser;

import de.tu_berlin.cit.intercloud.webapp.pages.IBrowserPage;
import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * This panel provides an address bar which allows to enter the resources path
 * of an XMPP entity.
 */
public class AddressBarPanel extends Panel {
    private IModel<String> restPath;

    public AddressBarPanel(String id, IModel<XmppURI> addressModel, IBrowserPage browserPage) {
        super(id);
        XmppURI xmppURI = addressModel.getObject();
        this.restPath = Model.of(xmppURI.getPath());

        Form form = new Form("browseForm");
        form.add(new Label("jid", xmppURI.getJID()));
        form.add(new TextField<>("restPath", restPath).setRequired(true));

        AjaxButton button = new AjaxButton("browseBtn") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                browserPage.browse(xmppURI.getJID(), restPath.getObject());
                setResponsePage(browserPage);
            }
        };

        form.add(button);
        form.setDefaultButton(button);
        this.add(form);
    }
}
