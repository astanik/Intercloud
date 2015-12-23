package de.tu_berlin.cit.intercloud.client.model.rest.action;

import java.util.List;

public class ActionModel {
    private final String name;
    private final String documentation;
    private final List<ParameterModel> parameterList;
    private final ParameterModel result;

    public ActionModel(String name, String documentation, List<ParameterModel> parameterList, ParameterModel result) {
        this.name = name;
        this.documentation = documentation;
        this.parameterList = parameterList;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public List<ParameterModel> getParameterList() {
        return parameterList;
    }

    public ParameterModel getResult() {
        return result;
    }
}
