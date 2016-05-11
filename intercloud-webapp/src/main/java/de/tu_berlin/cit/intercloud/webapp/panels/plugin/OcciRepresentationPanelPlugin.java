package de.tu_berlin.cit.intercloud.webapp.panels.plugin;

import de.tu_berlin.cit.intercloud.client.model.occi.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.webapp.panels.request.OcciRequestPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.OcciResponsePanel;
import org.apache.wicket.model.Model;

public class OcciRepresentationPanelPlugin implements IRepresentationPanelPlugin<OcciRepresentationModel> {
    @Override
    public Class<OcciRepresentationModel> getRepresentationModelClass() {
        return OcciRepresentationModel.class;
    }

    @Override
    public OcciRequestPanel getRequestPanel(String markupId, OcciRepresentationModel representationModel) {
        return new OcciRequestPanel(markupId, Model.of(representationModel));
    }

    @Override
    public OcciResponsePanel getResponsePanel(String markupId, OcciRepresentationModel representationModel) {
        return new OcciResponsePanel(markupId, Model.of(representationModel));
    }
}
