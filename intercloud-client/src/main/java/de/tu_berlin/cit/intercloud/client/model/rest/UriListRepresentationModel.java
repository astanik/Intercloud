package de.tu_berlin.cit.intercloud.client.model.rest;

import java.util.Arrays;
import java.util.List;

public class UriListRepresentationModel extends AbstractRepresentationModel {
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
