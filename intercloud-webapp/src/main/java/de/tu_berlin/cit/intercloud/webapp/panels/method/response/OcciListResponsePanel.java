package de.tu_berlin.cit.intercloud.webapp.panels.method.response;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.OcciListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.OcciRepresentationModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

/**
 * Displays an {@link OcciListRepresentationModel} in the context of a response
 * and thus the representation of media type {@code xml/occi-list}.
 */
public class OcciListResponsePanel extends Panel {
    public OcciListResponsePanel(String markupId, Model<OcciListRepresentationModel> representationModel) {
        super(markupId);

        this.add(new ListView<OcciRepresentationModel>("occiContainer",
                new ListModel<>(representationModel.getObject().getOcciRepresentationModels())) {
            @Override
            protected void populateItem(ListItem<OcciRepresentationModel> listItem) {
                listItem.add(new OcciResponsePanel("occiPanel", listItem.getModel()));
            }
        });
    }
}
