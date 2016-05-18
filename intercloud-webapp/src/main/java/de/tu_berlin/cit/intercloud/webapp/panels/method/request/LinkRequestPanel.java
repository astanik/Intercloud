package de.tu_berlin.cit.intercloud.webapp.panels.method.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.LinkModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Displays a {@link LinkModel} for the purpose of a request, without its
 * {@link de.tu_berlin.cit.intercloud.client.model.representation.occi.MixinModel}s.
 */
public class LinkRequestPanel extends CategoryRequestPanel {
    public LinkRequestPanel(String markupId, IModel<LinkModel> linkModel) {
        super(markupId, linkModel);

        LinkModel link = linkModel.getObject();
        // display link specific properties
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
