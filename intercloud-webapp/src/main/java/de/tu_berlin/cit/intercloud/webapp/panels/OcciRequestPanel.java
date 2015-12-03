package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.OcciRepresentationModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import java.util.ArrayList;

public class OcciRequestPanel extends Panel {
    public OcciRequestPanel(String id, IModel<MethodModel> methodModel, IModel<OcciRepresentationModel> representationModel) {
        super(id);

        OcciRepresentationModel representation= representationModel.getObject();
        KindModel kindModel = representation.getKind();
        if (null != kindModel) {
            this.add(new CategoryRequestPanel("kindPanel", methodModel, new Model<>(kindModel)));
        } else {
            this.add(new EmptyPanel("kindPanel"));
        }

        this.add(new ListView<MixinModel>("mixinContainer", new ListModel<>(new ArrayList<>(representation.getMixins()))) {
            @Override
            protected void populateItem(ListItem<MixinModel> listItem) {
                listItem.add(new CategoryRequestPanel("mixinPanel", methodModel, listItem.getModel()));
            }
        });
    }
}
