package de.tu_berlin.cit.intercloud.webapp.panels.request;

import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.MethodModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class LinkRequestPanel extends CategoryRequestPanel {
    public LinkRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<LinkModel> linkModel) {
        super(markupId, methodModel, linkModel);

        LinkModel link = linkModel.getObject();
        getContainer().add(new Label("relation", link.getRelates()));
        getContainer().add(new TextField<>("target", new PropertyModel<>(link, "target"))
                .add(new AjaxFormComponentUpdatingBehavior("blur") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                        // do nothing, just apply model
                    }
                }));
    }

    @Override
    public String getType() {
        return "Link";
    }
}
