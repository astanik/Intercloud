package de.tu_berlin.cit.intercloud.webapp.panels.request;

import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class MixinRequestPanel extends CategoryRequestPanel {
    public MixinRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<MixinModel> mixinModel) {
        super(markupId, methodModel, mixinModel);

        getContainer().add(new Label("applies", mixinModel.getObject().getApplies()));
    }

    @Override
    public String getType() {
        return "Mixin";
    }
}
