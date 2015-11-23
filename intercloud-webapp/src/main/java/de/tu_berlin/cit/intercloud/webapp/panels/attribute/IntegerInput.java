package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.PropertyModel;

class IntegerInput extends NumberInput {
    public IntegerInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new NumberTextField<Integer>("attributeValue", new PropertyModel<>(attribute, "integer"))
                .setRequired(attribute.isRequired()));
    }
}
