package de.tu_berlin.cit.intercloud.client.model.representation.occi;

/**
 * Domain Model of {@link de.tu_berlin.cit.intercloud.occi.core.xml.classification.MixinClassification}
 * and {@link de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType}.
 */
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
