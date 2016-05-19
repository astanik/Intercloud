package de.tu_berlin.cit.intercloud.webapp.panels.method.response;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.MixinModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Displays a {@link MixinModel} in the context of a response.
 */
public class MixinResponsePanel extends CategoryResponsePanel {
    public MixinResponsePanel(String id, IModel<MixinModel> mixinModel) {
        super(id, mixinModel);
        add(new Label("applies", mixinModel.getObject().getApplies()));
    }

    @Override
    protected String getType() {
        return "Mixin";
    }
}
