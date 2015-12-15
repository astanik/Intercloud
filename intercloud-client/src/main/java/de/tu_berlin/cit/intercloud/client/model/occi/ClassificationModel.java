package de.tu_berlin.cit.intercloud.client.model.occi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClassificationModel {
    private KindModel kind;
    private Map<String, MixinModel> mixinMap = new HashMap<>();
    private Map<String, LinkModel> linkMap = new HashMap<>();

    public ClassificationModel() {
    }

    public ClassificationModel(KindModel kind, Map<String, MixinModel> mixinMap, Map<String, LinkModel> linkMap) {
        this.kind = kind;
        this.mixinMap = mixinMap;
        this.linkMap = linkMap;
    }

    public KindModel getKind() {
        return this.kind;
    }

    public void setKind(KindModel kind) {
        this.kind = kind;
    }

    public MixinModel getMixin(String id) {
        return this.mixinMap.get(id);
    }

    public Collection<MixinModel> getMixins() {
        return this.mixinMap.values();
    }

    public void addMixin(MixinModel mixin) {
        this.mixinMap.put(mixin.getId(), mixin);
    }

    public LinkModel getLink(String id) {
        return this.linkMap.get(id);
    }

    public Collection<LinkModel> getLinks() {
        return this.linkMap.values();
    }

    public void addLink(LinkModel link) {
        this.linkMap.put(link.getId(), link);
    }
}
