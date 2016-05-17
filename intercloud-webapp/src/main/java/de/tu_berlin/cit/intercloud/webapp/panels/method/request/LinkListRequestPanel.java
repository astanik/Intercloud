package de.tu_berlin.cit.intercloud.webapp.panels.method.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.LinkModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import java.util.List;

public class LinkListRequestPanel extends Panel{
    public LinkListRequestPanel(String markupId, IModel<List<LinkModel>> linkModelList) {
        super(markupId);

        this.add(new ListView<LinkModel>("linkContainer", linkModelList) {
            @Override
            protected void populateItem(ListItem<LinkModel> listItem) {
                listItem.add(new LinkRequestPanel("linkPanel", listItem.getModel()));
                listItem.add(new MixinListRequestPanel("mixinListPanel", new ListModel<>(listItem.getModelObject().getMixins())));
            }
        });

    }
}
