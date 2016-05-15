package de.tu_berlin.cit.intercloud.client.model.representation.occi;

import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;

import java.util.List;

/**
 * Domain Model for media type {@code xml/occi-list} and
 * {@link de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryListDocument.CategoryList}.
 */
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
