package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import org.apache.wicket.model.IModel;

public class MixinRequestPanel extends CategoryRequestPanel {
    public MixinRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<MixinModel> categoryModel) {
        super(markupId, methodModel, categoryModel);
    }

    @Override
    public String getType() {
        return "Mixin";
    }
}
