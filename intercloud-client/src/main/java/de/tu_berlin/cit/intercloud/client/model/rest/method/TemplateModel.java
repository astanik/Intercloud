package de.tu_berlin.cit.intercloud.client.model.rest.method;

import org.apache.xmlbeans.XmlObject;

import java.io.Serializable;

/**
 * Represents a Template provided by a {@link de.tu_berlin.cit.rwx4j.xwadl.RequestDocument.Request}.
 * It is identified by its {@code name}.
 * The {@code reference} points to the actual Template Representation.
 */
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
