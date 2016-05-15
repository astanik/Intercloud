package de.tu_berlin.cit.intercloud.client.model.representation.occi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Domain Model of {@link de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument.Classification}.
 */
public class ClassificationModel implements IMixinModelContainer {
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

    public MixinModel getMixin(String schema, String term) {
        return getMixin(schema + term);
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

    public LinkModel getLink(String schema, String term) {
        return getLink(schema + term);
    }

    public Collection<LinkModel> getLinks() {
        return this.linkMap.values();
    }

    public void addLink(LinkModel link) {
        this.linkMap.put(link.getId(), link);
    }
}
