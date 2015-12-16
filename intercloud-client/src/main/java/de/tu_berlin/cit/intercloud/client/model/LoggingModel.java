package de.tu_berlin.cit.intercloud.client.model;

import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

import java.io.Serializable;

public class LoggingModel implements Serializable {
    private static final long serialVersionUID = -3148741249548656428L;

    private String xwadlDocument;
    private String restDocument;

    public String getXwadlDocument() {
        return xwadlDocument;
    }

    public void setXwadlDocument(String xwadlDocument) {
        this.xwadlDocument = xwadlDocument;
    }

    public void setXwadlDocument(ResourceTypeDocument xwadlDocument) {
        this.setXwadlDocument(xwadlDocument.toString());
    }

    public String getRestDocument() {
        return restDocument;
    }

    public void setRestDocument(String restDocument) {
        this.restDocument = restDocument;
    }

    public void setRestDocument(ResourceDocument restDocument) {
        this.setRestDocument(restDocument.toString());
    }
}
