package de.tu_berlin.cit.intercloud.client.model;

import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;

import java.util.Collection;

public interface IMixinModelContainer {
    void addMixin(MixinModel mixinModel);
    Collection<MixinModel> getMixins();
}
