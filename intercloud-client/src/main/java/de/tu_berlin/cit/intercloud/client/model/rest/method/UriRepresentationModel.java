package de.tu_berlin.cit.intercloud.client.model.rest.method;

public class UriRepresentationModel implements IRepresentationModel {
    private static final long serialVersionUID = 491682385199857076L;

    String uri;

    public UriRepresentationModel() {
    }

    public UriRepresentationModel(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
