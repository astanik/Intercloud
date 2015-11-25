package de.tu_berlin.cit.intercloud.client.model.occi;

import java.util.ArrayList;
import java.util.List;

public class LinkModel extends CategoryModel {
    private String target;
    private List<MixinModel> mixins = new ArrayList<>();

    public LinkModel(String term, String schema) {
        super(term, schema);
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
