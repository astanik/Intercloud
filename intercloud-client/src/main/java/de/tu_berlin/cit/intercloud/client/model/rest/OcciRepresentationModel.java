package de.tu_berlin.cit.intercloud.client.model.rest;

import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;

import java.util.List;

public class OcciRepresentationModel extends AbstractRepresentationModel {
    private KindModel kindModel;
    private List<MixinModel> mixinModels;
    private List<LinkModel> linkModels;

    public KindModel getKindModel() {
        return kindModel;
    }

    public void setKindModel(KindModel kindModel) {
        this.kindModel = kindModel;
    }

    public List<MixinModel> getMixinModels() {
        return mixinModels;
    }

    public void setMixinModels(List<MixinModel> mixinModels) {
        this.mixinModels = mixinModels;
    }

    public List<LinkModel> getLinkModels() {
        return linkModels;
    }

    public void setLinkModels(List<LinkModel> linkModels) {
        this.linkModels = linkModels;
    }
}
