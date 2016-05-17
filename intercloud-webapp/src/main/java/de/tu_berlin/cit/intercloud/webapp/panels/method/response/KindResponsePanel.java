package de.tu_berlin.cit.intercloud.webapp.panels.method.response;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.KindModel;
import org.apache.wicket.model.IModel;

public class KindResponsePanel extends CategoryResponsePanel {
    public KindResponsePanel(String id, IModel<KindModel> kindModel) {
        super(id, kindModel);
    }

    @Override
    protected String getType() {
        return "Kind";
    }
}
