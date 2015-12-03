package de.tu_berlin.cit.intercloud.client.model.rest;

import de.tu_berlin.cit.intercloud.client.model.IMixinModelContainer;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OcciRepresentationModel extends AbstractRepresentationModel implements IMixinModelContainer {
    private KindModel kind;
    private final Map<String, MixinModel> mixinMap = new HashMap<>();
    private Collection<LinkModel> links;

    public KindModel getKind() {
        return kind;
    }

    public void setKind(KindModel kind) {
        this.kind = kind;
    }

    public void addMixin(MixinModel mixin) {
        this.mixinMap.put(mixin.getId(), mixin);
    }

    public MixinModel getMixin(String mixinId) {
        return this.mixinMap.get(mixinId);
    }

    public Collection<MixinModel> getMixins() {
        return mixinMap.values();
    }

    public Collection<LinkModel> getLinks() {
        return links;
    }

    public void setLinks(Collection<LinkModel> links) {
        this.links = links;
    }
}
