package de.tu_berlin.cit.intercloud.webapp.panels.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.KindModel;
import org.apache.wicket.model.IModel;

public class KindRequestPanel extends CategoryRequestPanel {
    public KindRequestPanel(String markupId, IModel<KindModel> kindModel) {
        super(markupId, kindModel);
    }

    @Override
    public String getType() {
        return "Kind";
    }
}
