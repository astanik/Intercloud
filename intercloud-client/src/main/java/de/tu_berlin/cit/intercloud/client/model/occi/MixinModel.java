package de.tu_berlin.cit.intercloud.client.model.occi;

public class MixinModel extends CategoryModel {
    private final String[] applies;

    public MixinModel(String term, String schema, String[] applies) {
        super(term, schema);
        this.applies = applies;
    }

    public String[] getApplies() {
        return applies;
    }
}
