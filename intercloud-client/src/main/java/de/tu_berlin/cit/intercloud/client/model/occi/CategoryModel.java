package de.tu_berlin.cit.intercloud.client.model.occi;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class CategoryModel implements Serializable {
    private static final long serialVersionUID = -158045063500836807L;

    private final String term;
    private final String schema;
    private final Map<String, AttributeModel> attributes = new LinkedHashMap<>();
    private final Set<String> templates = new LinkedHashSet<>();

    private String title;

    public CategoryModel(String term, String schema) {
        this.term = term;
        this.schema = schema;
    }

    public String getTerm() {
        return this.term;
    }

    public String getSchema() {
        return this.schema;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addAttribute(AttributeModel attribute) {
        this.attributes.put(attribute.getName(), attribute);
    }

    public AttributeModel getAttribute(String name) {
        return this.attributes.get(name);
    }

    public Collection<AttributeModel> getAttributes() {
       return this.attributes.values();
    }

    public void addTemplate(String templateTitle) {
        addTemplate(templateTitle);
    }

    public Collection<String> getTemplates() {
        return this.templates;
    }
}
