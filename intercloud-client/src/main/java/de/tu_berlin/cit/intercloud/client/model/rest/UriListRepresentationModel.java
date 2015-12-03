package de.tu_berlin.cit.intercloud.client.model.rest;

import java.util.List;

public class UriListRepresentationModel extends AbstractRepresentationModel {
    List<String> uriList;

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }

    public List<String> getUriList() {
        return uriList;
    }
}
