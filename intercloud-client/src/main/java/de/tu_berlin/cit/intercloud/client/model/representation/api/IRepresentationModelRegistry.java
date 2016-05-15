package de.tu_berlin.cit.intercloud.client.model.representation.api;

import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModelPlugin;

public interface IRepresentationModelRegistry {
    IRepresentationModelPlugin getPlugin(String mediaType);

    void registerPlugin(IRepresentationModelPlugin plugin);
}
