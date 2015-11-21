package de.tu_berlin.cit.intercloud.xmpp.client.occi.representation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Category implements Serializable {
    private static final long serialVersionUID = -158045063500836807L;

    private final String term;
    private final String schema;
    private final List<Attribute> attributes = new ArrayList<>();

    public Category(String term, String schema) {
        this.term = term;
        this.schema = schema;
    }

    public String getTerm() {
        return term;
    }

    public String getSchema() {
        return schema;
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public List<Attribute> getAttributes() {
       return attributes;
    }
}
