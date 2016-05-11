package de.tu_berlin.cit.intercloud.client.model.occi;

import java.util.Collection;

/**
 * Every OCCI Model which can contain {@link MixinModel}s
 * may implement this interface.
 */
public interface IMixinModelContainer {
    void addMixin(MixinModel mixinModel);
    Collection<MixinModel> getMixins();
}
