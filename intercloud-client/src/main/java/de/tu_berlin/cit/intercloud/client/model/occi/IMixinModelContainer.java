package de.tu_berlin.cit.intercloud.client.model.occi;

import java.util.Collection;

public interface IMixinModelContainer {
    void addMixin(MixinModel mixinModel);
    Collection<MixinModel> getMixins();
}
