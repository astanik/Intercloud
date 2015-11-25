package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.Attribute;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.PropertyModel;

class DoubleInput extends NumberInput {
    public DoubleInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new NumberTextField<Double>("attributeValue", new PropertyModel<>(attribute, "double"))
                .setRequired(attribute.isRequired()));
    }
}
