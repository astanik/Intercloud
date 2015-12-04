package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

class ListInput extends TextareaInput {
    public ListInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
        this.add(new TextArea<>("attributeValue", new PropertyModel<>(attribute, "list"))
                .setRequired(attribute.isRequired()).add(new PlaceholderBehavior("item_1; item_2; ...")));
    }
}
