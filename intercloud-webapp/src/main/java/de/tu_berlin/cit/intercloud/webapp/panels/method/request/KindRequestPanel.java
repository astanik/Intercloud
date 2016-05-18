package de.tu_berlin.cit.intercloud.webapp.panels.method.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.KindModel;
import org.apache.wicket.model.IModel;

/**
 * Displays a {@link KindModel} for the purpose of a request.
 */
public class KindRequestPanel extends CategoryRequestPanel {
    public KindRequestPanel(String markupId, IModel<KindModel> kindModel) {
        super(markupId, kindModel);
    }

    @Override
    public String getType() {
        return "Kind";
    }
}
