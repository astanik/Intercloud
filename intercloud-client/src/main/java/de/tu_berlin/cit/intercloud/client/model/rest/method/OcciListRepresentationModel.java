package de.tu_berlin.cit.intercloud.client.model.rest.method;

import java.util.List;

public class OcciListRepresentationModel extends AbstractRepresentationModel {
    private List<OcciRepresentationModel> occiRepresentationModels;

    public OcciListRepresentationModel(List<OcciRepresentationModel> occiRepresentationModels) {
        this.occiRepresentationModels = occiRepresentationModels;
    }

    public List<OcciRepresentationModel> getOcciRepresentationModels() {
        return occiRepresentationModels;
    }
}
