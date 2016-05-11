package de.tu_berlin.cit.intercloud.webapp.panels.plugin;

import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModel;
import org.apache.wicket.markup.html.panel.Panel;

public interface IRepresentationPanelPlugin<R extends IRepresentationModel> {

    Class<R> getRepresentationModelClass();

    Panel getRequestPanel(String markupId, R representationModel);

    Panel getResponsePanel(String markupId, R representationModel);
}