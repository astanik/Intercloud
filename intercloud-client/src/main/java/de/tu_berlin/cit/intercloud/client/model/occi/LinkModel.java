package de.tu_berlin.cit.intercloud.client.model.occi;

import java.util.ArrayList;
import java.util.List;

public class LinkModel extends CategoryModel implements IMixinModelContainer {
    private final List<MixinModel> mixinList = new ArrayList<>();
    private final String relates;
    private String target;

    public LinkModel(String schema, String term, String relates) {
        super(schema, term);
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

    @Override
    public void addMixin(MixinModel mixin) {
        this.mixinList.add(mixin);
    }

    @Override
    public List<MixinModel> getMixins() {
        return mixinList;
    }
}
