package de.tu_berlin.cit.intercloud.webapp.panels.plugin.api;

import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;

public interface IRepresentationPanelRegistry {

    IRepresentationPanelPlugin getPlugin(Class<? extends IRepresentationModel> clazz);

    IRepresentationPanelPlugin getPlugin(IRepresentationModel instance);

    void registerPlugin(IRepresentationPanelPlugin plugin);
}
