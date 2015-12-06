package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import org.apache.wicket.model.IModel;

public class LinkRequestPanel extends CategoryRequestPanel {
    public LinkRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<LinkModel> categoryModel) {
        super(markupId, methodModel, categoryModel);
    }

    @Override
    public String getType() {
        return "Link";
    }
}
