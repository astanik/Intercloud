package de.tu_berlin.cit.intercloud.client.model.representation.impl;

import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.OcciListRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.OcciRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModelRegistry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RepresentationModelRegistry implements IRepresentationModelRegistry {
    private static final RepresentationModelRegistry INSTANCE = new RepresentationModelRegistry();

    private final ConcurrentMap<String, IRepresentationModelPlugin> pluginMap;

    private RepresentationModelRegistry() {
        pluginMap = new ConcurrentHashMap<>();
        registerPlugin(new TextRepresentationModelPlugin());
        registerPlugin(new UriRepresentationModelPlugin());
        registerPlugin(new UriListRepresentationModelPlugin());
        registerPlugin(new OcciRepresentationModelPlugin());
        registerPlugin(new OcciListRepresentationModelPlugin());
    }

    public static RepresentationModelRegistry getInstance() {
        return INSTANCE;
    }

    public IRepresentationModelPlugin getPlugin(String mediaType) {
        return pluginMap.get(mediaType);
    }

    public void registerPlugin(IRepresentationModelPlugin plugin) {
        pluginMap.put(plugin.getMediaType(), plugin);
    }
}
