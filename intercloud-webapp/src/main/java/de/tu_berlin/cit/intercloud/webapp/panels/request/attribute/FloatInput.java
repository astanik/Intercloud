package de.tu_berlin.cit.intercloud.webapp.panels.request.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.PropertyModel;

class FloatInput extends NumberInput {
    public FloatInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
        this.add(new NumberTextField<Float>("attributeValue", new PropertyModel<>(attribute, "float"))
                .setRequired(attribute.isRequired()));
    }
}