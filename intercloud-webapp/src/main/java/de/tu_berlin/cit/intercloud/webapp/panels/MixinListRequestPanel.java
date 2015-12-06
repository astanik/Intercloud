package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class MixinListRequestPanel extends Panel{
    public MixinListRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<List<MixinModel>> mixinModelList) {
        super(markupId);

        this.add(new ListView<MixinModel>("mixinContainer", mixinModelList) {

            @Override
            protected void populateItem(ListItem<MixinModel> listItem) {
                listItem.add(new MixinRequestPanel("mixinPanel", methodModel, listItem.getModel()));
            }
        });
    }
}
