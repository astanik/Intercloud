package de.tu_berlin.cit.intercloud.client.model.occi;

import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModel;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain Model for media type {@code xml/occi} and
 * {@link de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument.Category}
 */
public class OcciRepresentationModel implements IRepresentationModel, IMixinModelContainer {
    private static final long serialVersionUID = -7696772280697269027L;

    private KindModel kind;
    private final List<MixinModel> mixinList;
    /**
     * List of Link Models contained in the actual Representation.
     */
    private final List<LinkModel> linkList;
    /**
     * List of Link Models defined by the Classification Model.
     * May be provided to a user to select Links.
     */
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
