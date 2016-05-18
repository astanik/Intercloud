package de.tu_berlin.cit.intercloud.webapp.panels.method.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.MixinModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Displays a list of {@link MixinModel}s for the purpose of a request.
 */
public class MixinListRequestPanel extends Panel{
    public MixinListRequestPanel(String markupId, IModel<List<MixinModel>> mixinModelList) {
        super(markupId);

        this.add(new ListView<MixinModel>("mixinContainer", mixinModelList) {

            @Override
            protected void populateItem(ListItem<MixinModel> listItem) {
                listItem.add(new MixinRequestPanel("mixinPanel", listItem.getModel()));
            }
        });
    }
}
