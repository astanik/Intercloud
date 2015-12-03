package de.tu_berlin.cit.intercloud.client.model.occi;

import de.tu_berlin.cit.intercloud.client.model.IMixinModelContainer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LinkModel extends CategoryModel implements IMixinModelContainer {
    private final Map<String, MixinModel> mixinMap = new HashMap<>();
    private final String relates;
    private String target;

    public LinkModel(String term, String schema, String relates) {
        super(term, schema);
        this.relates = relates;
    }

    public String getRelates() {
        return relates;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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
}
