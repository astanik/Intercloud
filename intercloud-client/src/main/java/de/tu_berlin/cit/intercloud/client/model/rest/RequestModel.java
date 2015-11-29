package de.tu_berlin.cit.intercloud.client.model.rest;

import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;

import java.io.Serializable;
import java.util.List;

public class RequestModel implements Serializable {
    private static final long serialVersionUID = -2469751830303450595L;

    private KindModel kindModel;
    private List<MixinModel> mixinModels;

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
}
