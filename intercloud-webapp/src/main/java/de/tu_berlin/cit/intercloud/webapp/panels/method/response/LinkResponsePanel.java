package de.tu_berlin.cit.intercloud.webapp.panels.method.response;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.LinkModel;
import de.tu_berlin.cit.intercloud.webapp.pages.BrowserPage;
import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class LinkResponsePanel extends CategoryResponsePanel {
    private static final Logger logger = LoggerFactory.getLogger(LinkResponsePanel.class);

    public LinkResponsePanel(String id, IModel<LinkModel> linkModel) {
        super(id, linkModel);

        LinkModel link = linkModel.getObject();
        this.add(new Link("target") {
            @Override
            public void onClick() {
                try {
                    XmppURI uri = new XmppURI(link.getTarget());
                    setResponsePage(new BrowserPage(Model.of(uri)));
                } catch (URISyntaxException e) {
                    logger.error("Failed to redirect.", e);
                }
            }
        }.setBody(Model.of(link.getTarget())));
    }

    @Override
    protected String getType() {
        return "Link";
    }
}
