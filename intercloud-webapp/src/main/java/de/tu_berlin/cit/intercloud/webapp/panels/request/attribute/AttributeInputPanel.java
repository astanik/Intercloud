package de.tu_berlin.cit.intercloud.webapp.panels.request.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class AttributeInputPanel extends Panel {

    public AttributeInputPanel(String markupId, IModel<List<AttributeModel>> attributesModel) {
        super(markupId);

        this.add(new ListView<AttributeModel>("attributeList", attributesModel) {
            @Override
            protected void populateItem(ListItem<AttributeModel> listItem) {
                listItem.add(AbstractAttributeInput.newInstance("attributeInput", listItem.getModelObject()));
            }
        });
    }
}
