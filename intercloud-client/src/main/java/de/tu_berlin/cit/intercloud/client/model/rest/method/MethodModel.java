package de.tu_berlin.cit.intercloud.client.model.rest.method;

import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.xwadl.MethodDocument;

import java.io.Serializable;

public class MethodModel implements Serializable {
    private static final long serialVersionUID = 1297202550449311014L;

    public class RequestResponseModel implements Serializable {
        private static final long serialVersionUID = 8836550349575306648L;

        private final String mediaType;
        private final String documentation;

        public RequestResponseModel(String mediaType, String documentation) {
            this.mediaType = mediaType;
            this.documentation = documentation;
        }

        public String getMediaType() {
            return mediaType;
        }

        public String getDocumentation() {
            return documentation;
        }
    }

    private final XmppURI uri;

    private final String type;
    private final RequestResponseModel request;
    private final RequestResponseModel response;
    private final String documentation;
    private final MethodDocument.Method reference;

    public MethodModel(XmppURI uri, MethodDocument.Method method) {
        this.uri = uri;
        this.reference = method;
        this.type = method.getType().toString();
        if (method.isSetRequest()) {
            if (null != method.getRequest().getDocumentation()) {
                this.request = new RequestResponseModel(method.getRequest().getMediaType(),
                        method.getRequest().getDocumentation().getStringValue());
            } else {
                this.request = new RequestResponseModel(method.getRequest().getMediaType(), null);
            }
        } else {
            this.request = null;
        }
        if (method.isSetResponse()) {
            if (null != method.getResponse().getDocumentation()) {
                this.response = new RequestResponseModel(method.getResponse().getMediaType(),
                        method.getResponse().getDocumentation().getStringValue());
            } else {
                this.response = new RequestResponseModel(method.getResponse().getMediaType(), null);
            }
        } else {
            this.response = null;
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

    public RequestResponseModel getRequest() {
        return request;
    }

    public RequestResponseModel getResponse() {
        return response;
    }

    public String getRequestMediaType() {
        return null != this.request ? this.request.getMediaType() : null;
    }

    public String getResponseMediaType() {
        return null != this.response ? this.response.getMediaType() : null;
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
                ", requestMediaType='" + getRequestMediaType() + '\'' +
                "responseMediaType='" + getResponseMediaType() + '\'' +
                ", type='" + type + '\'' +
                ", uri=" + uri +
                '}';
    }
}
