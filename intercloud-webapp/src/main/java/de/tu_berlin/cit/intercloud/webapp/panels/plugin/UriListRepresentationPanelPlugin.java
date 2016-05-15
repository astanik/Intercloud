package de.tu_berlin.cit.intercloud.webapp.panels.plugin;

import de.tu_berlin.cit.intercloud.client.model.representation.impl.UriListRepresentationModel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.UriResponsePanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class UriListRepresentationPanelPlugin implements IRepresentationPanelPlugin<UriListRepresentationModel> {
    @Override
    public Class<UriListRepresentationModel> getRepresentationModelClass() {
        return UriListRepresentationModel.class;
    }

    @Override
    public Panel getRequestPanel(String markupId, UriListRepresentationModel representationModel) {
        return null;
    }

    @Override
    public UriResponsePanel getResponsePanel(String markupId, UriListRepresentationModel representationModel) {
        return new UriResponsePanel(markupId, Model.of(representationModel));
    }
}
