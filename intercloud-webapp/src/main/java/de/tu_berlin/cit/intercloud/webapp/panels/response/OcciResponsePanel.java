package de.tu_berlin.cit.intercloud.webapp.panels.response;

import de.tu_berlin.cit.intercloud.client.model.rest.method.OcciRepresentationModel;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

public class OcciResponsePanel extends Panel {
    public OcciResponsePanel(String markupId, IModel<OcciRepresentationModel> representationModel) {
        super(markupId);

        OcciRepresentationModel representation = representationModel.getObject();
        if (null != representation.getKind()) {
            this.add(new KindResponsePanel("kindPanel", Model.of(representation.getKind())));
        } else {
            this.add(new EmptyPanel("kindPanel"));
        }

        this.add(new MixinListResponsePanel("mixinListPanel", new ListModel<>(representation.getMixins())));
        this.add(new LinkListResponsePanel("linkListPanel", new ListModel<>(representation.getLinks())));
    }
}
