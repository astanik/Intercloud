package de.tu_berlin.cit.intercloud.client.model.rest;

import de.tu_berlin.cit.intercloud.client.model.IMixinModelContainer;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

public class OcciRepresentationModel extends AbstractRepresentationModel implements IMixinModelContainer {
    private KindModel kind;
    private final List<MixinModel> mixinList;
    // list of actual used links
    private final List<LinkModel> linkList;
    // map of link definitions
    private final List<LinkModel> linkDefinitionList = new ArrayList<>();

    public OcciRepresentationModel() {
        this(null, new ArrayList<>(), new ArrayList<>());
    }

    public OcciRepresentationModel(KindModel kind, List<MixinModel> mixinList, List<LinkModel> linkList) {
        this.linkList = linkList;
        this.mixinList = mixinList;
        this.kind = kind;
    }

    public KindModel getKind() {
        return kind;
    }

    public void setKind(KindModel kind) {
        this.kind = kind;
    }

    @Override
    public void addMixin(MixinModel mixin) {
        this.mixinList.add(mixin);
    }

    @Override
    public List<MixinModel> getMixins() {
        return this.mixinList;
    }

    public List<LinkModel> getLinkDefinitions() {
        return this.linkDefinitionList;
    }

    public List<LinkModel> getLinks() {
        return linkList;
    }

    public void addToLinkList(LinkModel link) {
        if (null != link) {
            LinkModel clone = SerializationUtils.clone(link);
            linkList.add(clone);
        }
    }
}
