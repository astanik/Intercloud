package de.tu_berlin.cit.intercloud.webapp.panels.response;

import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import java.util.List;

public class LinkListResponsePanel extends Panel {
    public LinkListResponsePanel(String id, IModel<List<LinkModel>> linkListModel) {
        super(id);

        this.add(new ListView<LinkModel>("linkContainer", linkListModel) {
            @Override
            protected void populateItem(ListItem<LinkModel> listItem) {
                listItem.add(new LinkResponsePanel("linkPanel", listItem.getModel()));
                listItem.add(new MixinListResponsePanel("mixinPanel", new ListModel<>(listItem.getModelObject().getMixins())));
            }
        });
    }
}
