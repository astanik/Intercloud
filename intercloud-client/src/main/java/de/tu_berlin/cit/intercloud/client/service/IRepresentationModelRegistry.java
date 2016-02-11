package de.tu_berlin.cit.intercloud.client.service;

import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModelPlugin;

public interface IRepresentationModelRegistry {
    IRepresentationModelPlugin getRepresentationModelPlugin(String mediaType);

    void registerRepresentationModelPlugin(IRepresentationModelPlugin plugin);
}
