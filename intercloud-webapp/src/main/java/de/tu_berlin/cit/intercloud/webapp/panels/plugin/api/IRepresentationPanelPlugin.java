package de.tu_berlin.cit.intercloud.webapp.panels.plugin.api;

import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * This interface is associated with a certain {@link IRepresentationModel} and thus a certain media type.
 * It provides an extension mechanism to dynamically create {@link Panel}s for a certain {@link IRepresentationModel}.
 * @param <R> The Representation Model that is associated with this plugin.
 */
public interface IRepresentationPanelPlugin<R extends IRepresentationModel> {

    /**
     * This method is used to register this plugin at the {@link IRepresentationPanelRegistry}.
     * @return The Class of the Representation Model that is associated with this plugin.
     */
    Class<R> getRepresentationModelClass();

    /**
     * @param markupId
     * @param representationModel
     * @return A Panel for the purpose of a request, thus expecting some user inputs.
     */
    Panel getRequestPanel(String markupId, R representationModel);

    /**
     * @param markupId
     * @param representationModel
     * @return A Panel for the purpose of a response, thus it diplays the resulting
     * representation model without any user inputs.
     */
    Panel getResponsePanel(String markupId, R representationModel);
}