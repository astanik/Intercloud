package de.tu_berlin.cit.intercloud.client.model.occi;

public class MixinModel extends CategoryModel {
    private final String applies;

    public MixinModel(String schema, String term, String applies) {
        super(schema, term);
        this.applies = applies;
    }

    public String getApplies() {
        return applies;
    }
}
