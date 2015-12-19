package de.tu_berlin.cit.intercloud.client.model;

import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

import java.io.Serializable;

public class LoggingModel implements Serializable {
    private static final long serialVersionUID = -3148741249548656428L;

    private String xwadl;
    private String restRequest;
    private String restResponse;

    public String getXwadl() {
        return xwadl;
    }

    public void setXwadl(String xwadl) {
        this.xwadl = xwadl;
    }

    public void setXwad(ResourceTypeDocument xwadlDocument) {
        this.setXwadl(xwadlDocument.toString());
    }

    public String getRestRequest() {
        return restRequest;
    }

    public void setRestRequest(String restRequest) {
        this.restRequest = restRequest;
    }

    public String getRestResponse() {
        return restResponse;
    }

    public void setRestResponse(String restResponse) {
        this.restResponse = restResponse;
    }

    public void setRestResponse(ResourceDocument restDocument) {
        this.setRestResponse(restDocument.toString());
    }
}
