package de.tu_berlin.cit.intercloud.client.model.rest.method;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument;

import java.io.Serializable;

public class MethodModel implements Serializable {
    private static final long serialVersionUID = 1297202550449311014L;

    private final XmppURI uri;

    private final String type;
    private final String requestMediaType;
    private final String responseMediaType;
    private final String documentation;
    private final MethodDocument.Method reference;

    public MethodModel(XmppURI uri, MethodDocument.Method method) {
        this.uri = uri;
        this.reference = method;
        this.type = method.getType().toString();
        if (method.isSetRequest()) {
            this.requestMediaType = method.getRequest().getMediaType();
        } else {
            this.requestMediaType = null;
        }
        if (method.isSetResponse()) {
            this.responseMediaType = method.getResponse().getMediaType();
        } else {
            this.responseMediaType = null;
        }
        if (null != method.getDocumentation()) {
            this.documentation = method.getDocumentation().getStringValue();
        } else {
            this.documentation = null;
        }
    }

    public XmppURI getUri() {
        return uri;
    }

    public String getType() {
        return type;
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

    public MethodDocument.Method getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "MethodModel{" +
                "responseMediaType='" + responseMediaType + '\'' +
                ", requestMediaType='" + requestMediaType + '\'' +
                ", type='" + type + '\'' +
                ", uri=" + uri +
                '}';
    }
}
