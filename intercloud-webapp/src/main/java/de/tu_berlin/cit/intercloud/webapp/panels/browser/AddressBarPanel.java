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
import org.apache.wicket.model.PropertyModel;

public class AddressBarPanel extends Panel {
    private String restPath;

    public AddressBarPanel(String id, IModel<XmppURI> addressModel, IBrowserPage browserPage) {
        super(id);
        XmppURI xmppURI = addressModel.getObject();
        this.restPath = xmppURI.getPath();

        Form form = new Form("browseForm");
        form.add(new Label("jid", xmppURI.getJID()));
        form.add(new TextField<String>("restPath", new PropertyModel<>(this, "restPath")).setRequired(true));

        AjaxButton button = new AjaxButton("browseBtn") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                browserPage.browse(xmppURI.getJID(), restPath);
                setResponsePage(browserPage);
            }
        };

        form.add(button);
        form.setDefaultButton(button);
        this.add(form);
    }
}
