package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.RequestModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

public class OcciRequestPanel extends Panel {
    public OcciRequestPanel(String id, IModel<MethodModel> methodModel, IModel<RequestModel> requestModel) {
        super(id);

        RequestModel request = requestModel.getObject();
        KindModel kindModel = request.getKindModel();
        if (null != kindModel) {
            this.add(new CategoryRequestPanel("kindPanel", methodModel, new Model<>(kindModel)));
        } else {
            this.add(new EmptyPanel("kindPanel"));
        }

        this.add(new ListView<MixinModel>("mixinContainer", new ListModel<>(request.getMixinModels())) {
            @Override
            protected void populateItem(ListItem<MixinModel> listItem) {
                listItem.add(new CategoryRequestPanel("mixinPanel", methodModel, listItem.getModel()));
            }
        });
    }
}
