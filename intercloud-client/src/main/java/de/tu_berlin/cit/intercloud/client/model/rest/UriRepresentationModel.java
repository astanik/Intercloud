package de.tu_berlin.cit.intercloud.client.model.rest;

import java.util.ArrayList;
import java.util.List;

public class UriRepresentationModel extends AbstractRepresentationModel {
    List<String> uriList = new ArrayList<>();

    public List<String> getUriList() {
        return uriList;
    }
}
