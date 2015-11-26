package de.tu_berlin.cit.intercloud.client.model.rest;

import java.io.Serializable;

public class MethodModel implements Serializable {
    private static final long serialVersionUID = 1297202550449311014L;

    private final String methodType;
    private final String requestMediaType;
    private final String responseMediaType;
    private final String documentation;

    public MethodModel(String methodType, String requestMediaType, String responseMediaType, String documentation) {
        this.methodType = methodType;
        this.requestMediaType = requestMediaType;
        this.responseMediaType = responseMediaType;
        this.documentation = documentation;
    }

    public String getMethodType() {
        return methodType;
    }

    public String getRequestMediaType() {
        return requestMediaType;
    }

    public boolean hasRequest() {
        return null != requestMediaType && !requestMediaType.trim().isEmpty();
    }

    public String getResponseMediaType() {
        return responseMediaType;
    }

    public String getDocumentation() {
        return documentation;
    }

    @Override
    public String toString() {
        return "MethodModel{" +
                "methodType='" + methodType + '\'' +
                ", requestMediaType='" + requestMediaType + '\'' +
                ", responseMediaType='" + responseMediaType + '\'' +
                '}';
    }
}
