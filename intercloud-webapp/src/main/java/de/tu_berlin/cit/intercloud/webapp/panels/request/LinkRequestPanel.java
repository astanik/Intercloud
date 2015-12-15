package de.tu_berlin.cit.intercloud.webapp.panels.request;

import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class LinkRequestPanel extends CategoryRequestPanel {
    public LinkRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<LinkModel> linkModel) {
        super(markupId, methodModel, linkModel);

        LinkModel link = linkModel.getObject();
        getContainer().add(new Label("relation", link.getRelates()));
        getContainer().add(new TextField<>("target", new PropertyModel<>(link, "target")));
    }

    @Override
    public String getType() {
        return "Link";
    }
}
