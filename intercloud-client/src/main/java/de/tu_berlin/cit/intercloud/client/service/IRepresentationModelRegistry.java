package de.tu_berlin.cit.intercloud.client.service;

import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModelPlugin;

public interface IRepresentationModelRegistry {
    IRepresentationModelPlugin getPlugin(String mediaType);

    void registerPlugin(IRepresentationModelPlugin plugin);
}
