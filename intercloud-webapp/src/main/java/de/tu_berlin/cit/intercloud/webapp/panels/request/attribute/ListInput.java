package de.tu_berlin.cit.intercloud.webapp.panels.request.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.components.ListTextArea;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.model.PropertyModel;

class ListInput extends TextareaInput {
    public ListInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
        this.add(new ListTextArea("attributeValue", new PropertyModel<>(attribute, "list"))
                .setRequired(attribute.isRequired()).add(new PlaceholderBehavior("item_1; item_2; ...")));
    }
}
