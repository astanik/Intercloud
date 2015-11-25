package de.tu_berlin.cit.intercloud.client.model.occi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class CategoryModel implements Serializable {
    private static final long serialVersionUID = -158045063500836807L;

    private final String term;
    private final String schema;
    private final List<AttributeModel> attributes = new ArrayList<>();

    public CategoryModel(String term, String schema) {
        this.term = term;
        this.schema = schema;
    }

    public String getTerm() {
        return term;
    }

    public String getSchema() {
        return schema;
    }

    public void addAttribute(AttributeModel attribute) {
        attributes.add(attribute);
    }

    public List<AttributeModel> getAttributes() {
       return attributes;
    }
}
