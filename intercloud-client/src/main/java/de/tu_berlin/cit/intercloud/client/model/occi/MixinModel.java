package de.tu_berlin.cit.intercloud.client.model.occi;

import java.util.Arrays;
import java.util.List;

public class MixinModel extends CategoryModel {
    private final List<String> applies;

    public MixinModel(String term, String schema, String[] applies) {
        super(term, schema);
        this.applies = Arrays.asList(applies);
    }

    public List<String> getApplies() {
        return applies;
    }
}
