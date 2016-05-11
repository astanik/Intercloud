package de.tu_berlin.cit.intercloud.webapp.panels.plugin;

import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RepresentationPanelRegistry implements IRepresentationPanelRegistry {
    private static final RepresentationPanelRegistry INSTANCE = new RepresentationPanelRegistry();

    public static RepresentationPanelRegistry getInstance() {
        return INSTANCE;
    }

    private final ConcurrentMap<Class<? extends IRepresentationModel>, IRepresentationPanelPlugin> pluginMap;

    private RepresentationPanelRegistry() {
        pluginMap = new ConcurrentHashMap<>();
        registerPlugin(new OcciListRepresentationPanelPlugin());
        registerPlugin(new OcciRepresentationPanelPlugin());
        registerPlugin(new UriListRepresentationPanelPlugin());
        registerPlugin(new UriRepresentationPanelPlugin());
    }

    @Override
    public IRepresentationPanelPlugin getPlugin(Class<? extends IRepresentationModel> clazz) {
        return pluginMap.get(clazz);
    }

    @Override
    public IRepresentationPanelPlugin getPlugin(IRepresentationModel instance) {
        return null != instance ? getPlugin(instance.getClass()) : null;
    }

    @Override
    public void registerPlugin(IRepresentationPanelPlugin plugin) {
        pluginMap.put(plugin.getRepresentationModelClass(), plugin);
    }
}
