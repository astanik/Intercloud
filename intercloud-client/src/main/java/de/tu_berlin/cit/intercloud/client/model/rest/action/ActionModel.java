package de.tu_berlin.cit.intercloud.client.model.rest.action;

public class ActionModel {
    private final String name;
    private final String documentation;

    public ActionModel(String name, String documentation) {
        this.name = name;
        this.documentation = documentation;
    }

    public String getName() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }
}
