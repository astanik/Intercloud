package de.tu_berlin.cit.intercloud.webapp.panels.method.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.MixinModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Displays a {@link MixinModel} for the purpose of a request.
 */
public class MixinRequestPanel extends CategoryRequestPanel {
    public MixinRequestPanel(String markupId, IModel<MixinModel> mixinModel) {
        super(markupId, mixinModel);
        // mixin specific properties
        getContainer().add(new Label("applies", mixinModel.getObject().getApplies()));
    }

    @Override
    public String getType() {
        return "Mixin";
    }
}
