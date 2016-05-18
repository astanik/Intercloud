package de.tu_berlin.cit.intercloud.webapp.panels.plugin.api;

import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;

/**
 * Implementations of this interface are used to provide {@link IRepresentationPanelPlugin}s
 * for a specific {@link IRepresentationModel} at runtime.
 */
public interface IRepresentationPanelRegistry {

    /**
     * @param clazz
     * @return A Representation Panel Plugin for a specific Representation Model.
     * The plugin must be registered first. If the Representation Model is not known
     * it returns null.
     */
    IRepresentationPanelPlugin getPlugin(Class<? extends IRepresentationModel> clazz);

    /**
     * @param instance
     * @return A Representation Panel Plugin for a specific Representation Model.
     * The plugin must be registered first. If the Representation Model is not known
     * it returns null.
     */
    IRepresentationPanelPlugin getPlugin(IRepresentationModel instance);

    /**
     * Registers an {@link IRepresentationPanelPlugin} by
     * {@link IRepresentationPanelPlugin#getRepresentationModelClass()}.
     * @param plugin
     */
    void registerPlugin(IRepresentationPanelPlugin plugin);
}
