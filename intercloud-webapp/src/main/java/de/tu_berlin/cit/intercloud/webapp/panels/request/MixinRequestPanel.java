package de.tu_berlin.cit.intercloud.webapp.panels.request;

import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class MixinRequestPanel extends CategoryRequestPanel {
    public MixinRequestPanel(String markupId, IModel<MixinModel> mixinModel) {
        super(markupId, mixinModel);

        getContainer().add(new Label("applies", mixinModel.getObject().getApplies()));
    }

    @Override
    public String getType() {
        return "Mixin";
    }
}
