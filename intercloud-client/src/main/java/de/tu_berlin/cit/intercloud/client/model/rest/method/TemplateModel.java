package de.tu_berlin.cit.intercloud.client.model.rest.method;

import org.apache.xmlbeans.XmlObject;

import java.io.Serializable;

public class TemplateModel implements Serializable {
    private static final long serialVersionUID = -6500317178110252569L;

    private String name;
    private XmlObject reference;

    public TemplateModel(String name, XmlObject reference) {
        this.name = name;
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public XmlObject getReference() {
        return reference;
    }
}
