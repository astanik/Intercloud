package de.tu_berlin.cit.intercloud.client.model.rest.method;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;

import java.io.Serializable;

public class MethodModel implements Serializable {
    private static final long serialVersionUID = 1297202550449311014L;

    private final XmppURI uri;

    private final String methodType;
    private final String requestMediaType;
    private final String responseMediaType;
    private final String documentation;

    public MethodModel(XmppURI uri, String methodType, String requestMediaType, String responseMediaType, String documentation) {
        this.uri = uri;
        this.methodType = methodType;
        this.requestMediaType = requestMediaType;
        this.responseMediaType = responseMediaType;
        this.documentation = documentation;
    }

    public XmppURI getUri() {
        return uri;
    }

    public String getMethodType() {
        return methodType;
    }

    public String getRequestMediaType() {
        return requestMediaType;
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
                "responseMediaType='" + responseMediaType + '\'' +
                ", requestMediaType='" + requestMediaType + '\'' +
                ", methodType='" + methodType + '\'' +
                ", uri=" + uri +
                '}';
    }
}
