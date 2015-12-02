package de.tu_berlin.cit.intercloud.client.model.rest;

import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;

import java.util.List;

public class OcciRepresentationModel extends AbstractRepresentationModel {
    private KindModel kindModel;
    private List<MixinModel> mixinModels;
    private List<LinkModel> linkModels;
}
