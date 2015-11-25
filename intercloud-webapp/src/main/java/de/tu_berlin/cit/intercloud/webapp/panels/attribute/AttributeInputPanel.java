package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class AttributeInputPanel extends Panel {

    public AttributeInputPanel(String markupId, IModel<List<Attribute>> attributesModel) {
        super(markupId);

        this.add(new ListView<Attribute>("attributeList", attributesModel) {
            @Override
            protected void populateItem(ListItem<Attribute> listItem) {
                listItem.add(AbstractAttributeInput.newInstance("attributeInput", listItem.getModelObject()));
            }
        });
    }
}
