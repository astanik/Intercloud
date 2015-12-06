package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import java.util.List;

public class LinkListRequestPanel extends Panel{
    public LinkListRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<List<LinkModel>> linkModelList) {
        super(markupId);

        this.add(new ListView<LinkModel>("linkContainer", linkModelList) {
            @Override
            protected void populateItem(ListItem<LinkModel> listItem) {
                listItem.add(new LinkRequestPanel("linkPanel", methodModel, listItem.getModel()));
                listItem.add(new MixinListRequestPanel("mixinListPanel", methodModel, new ListModel<MixinModel>(listItem.getModelObject().getMixins())));
            }
        });

    }
}
