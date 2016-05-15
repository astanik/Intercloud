package de.tu_berlin.cit.intercloud.webapp.panels.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.AttributeModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class AttributeListInputPanel extends Panel {

    public AttributeListInputPanel(String markupId, IModel<List<AttributeModel>> attributesModel) {
        super(markupId);

        this.add(new ListView<AttributeModel>("attributeList", attributesModel) {
            @Override
            protected void populateItem(ListItem<AttributeModel> listItem) {
                listItem.add(new AttributeInputPanel("attributeInput", listItem.getModel()));
            }
        });
    }
}
