package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.rest.UriListRepresentationModel;
import de.tu_berlin.cit.intercloud.webapp.pages.BrowserPage;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class UriResponsePanel extends Panel {
    private static final Logger logger = LoggerFactory.getLogger(UriResponsePanel.class);

    public UriResponsePanel(String id, IModel<UriListRepresentationModel> representationModel) {
        super(id);

        this.add(new ListView<String>("uriList", new ListModel<>(representationModel.getObject().getUriList())) {
            @Override
            protected void populateItem(ListItem<String> listItem) {
                AjaxLink link = new AjaxLink("uri") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            XmppURI uri = new XmppURI(listItem.getModelObject());
                            setResponsePage(new BrowserPage(Model.of(uri)));
                        } catch (URISyntaxException e) {
                            logger.error("Could not parse Xmpp Uri. {}", listItem.getModelObject(), e);
                            target.appendJavaScript("alert('Could not parse Xmpp Uri.');");
                        }
                    }
                };
                link.setBody(listItem.getModel());
                listItem.add(link);
            }
        });
    }
}
