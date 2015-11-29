package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.PropertyModel;

class IntegerInput extends NumberInput {
    public IntegerInput(String markupId, AttributeModel attribute, boolean enabled) {
        super(markupId, attribute, enabled);
        this.add(new NumberTextField<Integer>("attributeValue", new PropertyModel<>(attribute, "integer"))
                .setRequired(attribute.isRequired()).setEnabled(enabled));
    }
}
