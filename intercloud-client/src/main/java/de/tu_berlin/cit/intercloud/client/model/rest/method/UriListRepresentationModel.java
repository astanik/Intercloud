package de.tu_berlin.cit.intercloud.client.model.rest.method;

import java.util.Arrays;
import java.util.List;

/**
 * Domain Model for media type {@code text/uri-list}.
 */
public class UriListRepresentationModel implements IRepresentationModel {
    private static final long serialVersionUID = 7428305811510813898L;

    List<String> uriList;

    public UriListRepresentationModel(String... uriList) {
        this.uriList = Arrays.asList(uriList);
    }

    public UriListRepresentationModel() {
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }

    public List<String> getUriList() {
        return uriList;
    }
}
