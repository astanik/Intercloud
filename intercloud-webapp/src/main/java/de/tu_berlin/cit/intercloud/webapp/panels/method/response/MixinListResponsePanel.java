package de.tu_berlin.cit.intercloud.webapp.panels.method.response;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.MixinModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Displays a {@link List<MixinModel>} in the context of a response.
 */
public class MixinListResponsePanel extends Panel {
    public MixinListResponsePanel(String id, IModel<List<MixinModel>> mixinListModel) {
        super(id);

        this.add(new ListView<MixinModel>("mixinContainer", mixinListModel) {
            @Override
            protected void populateItem(ListItem<MixinModel> listItem) {
                listItem.add(new MixinResponsePanel("mixinPanel", listItem.getModel()));
            }
        });
    }
}
