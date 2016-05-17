package de.tu_berlin.cit.intercloud.client.model.action;

import de.tu_berlin.cit.rwx4j.xwadl.ActionDocument;

import java.io.Serializable;

/**
 * Domain Model of {@link de.tu_berlin.cit.rwx4j.xwadl.ActionDocument.Action}
 * and
 * {@link de.tu_berlin.cit.rwx4j.rest.ActionDocument.Action}.
 */
public class ActionModel implements Serializable {
    private static final long serialVersionUID = 5148561604973349432L;

    private final String name;
    private final String documentation;
    private final String resultDocumentation;
    private final ActionDocument.Action reference;

    public ActionModel(String name, String documentation, String resultDocumentation, ActionDocument.Action reference) {
        this.name = name;
        this.documentation = documentation;
        this.resultDocumentation = resultDocumentation;
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public String getResultDocumentation() {
        return resultDocumentation;
    }

    public ActionDocument.Action getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "ActionModel{" +
                "name='" + name + '\'' +
                '}';
    }
}