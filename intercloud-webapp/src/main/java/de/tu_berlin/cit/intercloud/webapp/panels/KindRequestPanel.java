package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import org.apache.wicket.model.IModel;

public class KindRequestPanel extends CategoryRequestPanel {
    public KindRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<KindModel> kindModel) {
        super(markupId, methodModel, kindModel);
    }

    @Override
    public String getType() {
        return "Kind";
    }
}
