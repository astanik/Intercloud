package de.tu_berlin.cit.intercloud.client.model.occi;

import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModel;

import java.util.List;

public class OcciListRepresentationModel implements IRepresentationModel {
    private static final long serialVersionUID = 5713372756643337932L;

    private List<OcciRepresentationModel> occiRepresentationModels;

    public OcciListRepresentationModel(List<OcciRepresentationModel> occiRepresentationModels) {
        this.occiRepresentationModels = occiRepresentationModels;
    }

    public List<OcciRepresentationModel> getOcciRepresentationModels() {
        return occiRepresentationModels;
    }
}
