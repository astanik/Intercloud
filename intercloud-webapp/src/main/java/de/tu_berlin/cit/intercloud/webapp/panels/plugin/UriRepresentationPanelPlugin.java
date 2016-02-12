package de.tu_berlin.cit.intercloud.webapp.panels.plugin;

import de.tu_berlin.cit.intercloud.client.model.rest.method.UriListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.UriRepresentationModel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.UriResponsePanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class UriRepresentationPanelPlugin implements IRepresentationPanelPlugin<UriRepresentationModel> {
    @Override
    public Class<UriRepresentationModel> getRepresentationModelClass() {
        return UriRepresentationModel.class;
    }

    @Override
    public Panel getRequestPanel(String markupId, UriRepresentationModel representationModel) {
        return null;
    }

    @Override
    public Panel getResponsePanel(String markupId, UriRepresentationModel representationModel) {
        return new UriResponsePanel(markupId,
                Model.of(new UriListRepresentationModel(representationModel.getUri())));
    }
}
