package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.occi.OcciListRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.occi.OcciRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.rest.method.TextRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.rest.method.UriListRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.rest.method.UriRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.service.IRepresentationModelRegistry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RepresentationModelRegistry implements IRepresentationModelRegistry {
    private static final RepresentationModelRegistry INSTANCE = new RepresentationModelRegistry();

    private final ConcurrentMap<String, IRepresentationModelPlugin> pluginMap;

    private RepresentationModelRegistry() {
        pluginMap = new ConcurrentHashMap<>();
        registerRepresentationModelPlugin(new TextRepresentationModelPlugin());
        registerRepresentationModelPlugin(new UriRepresentationModelPlugin());
        registerRepresentationModelPlugin(new UriListRepresentationModelPlugin());
        registerRepresentationModelPlugin(new OcciRepresentationModelPlugin());
        registerRepresentationModelPlugin(new OcciListRepresentationModelPlugin());
    }

    public static RepresentationModelRegistry getInstance() {
        return INSTANCE;
    }

    public IRepresentationModelPlugin getRepresentationModelPlugin(String mediaType) {
        return pluginMap.get(mediaType);
    }

    public void registerRepresentationModelPlugin(IRepresentationModelPlugin plugin) {
        pluginMap.put(plugin.getMediaType(), plugin);
    }
}
