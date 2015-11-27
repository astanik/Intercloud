package de.tu_berlin.cit.intercloud.client.model.occi;

import java.util.ArrayList;
import java.util.List;

public class LinkModel extends CategoryModel {
    private final List<MixinModel> mixins = new ArrayList<>();
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
        this.mixins.add(mixin);
    }

    public List<MixinModel> getMixins() {
        return mixins;
    }
}
