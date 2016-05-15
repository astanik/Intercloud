package de.tu_berlin.cit.intercloud.webapp.panels.plugin;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.OcciListRepresentationModel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.OcciListResponsePanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class OcciListRepresentationPanelPlugin implements IRepresentationPanelPlugin<OcciListRepresentationModel>{
    @Override
    public Class<OcciListRepresentationModel> getRepresentationModelClass() {
        return OcciListRepresentationModel.class;
    }

    @Override
    public Panel getRequestPanel(String markupId, OcciListRepresentationModel representationModel) {
        return null;
    }

    @Override
    public OcciListResponsePanel getResponsePanel(String markupId, OcciListRepresentationModel representationModel) {
        return new OcciListResponsePanel(markupId, Model.of(representationModel));
    }
}
