package de.tu_berlin.cit.intercloud.client.model.rest;

import java.util.List;

public class OcciListRepresentationModel extends AbstractRepresentationModel {
    private List<OcciRepresentationModel> occiRepresentationModels;

    public List<OcciRepresentationModel> getOcciRepresentationModels() {
        return occiRepresentationModels;
    }

    public void setOcciRepresentationModels(List<OcciRepresentationModel> occiRepresentationModels) {
        this.occiRepresentationModels = occiRepresentationModels;
    }
}