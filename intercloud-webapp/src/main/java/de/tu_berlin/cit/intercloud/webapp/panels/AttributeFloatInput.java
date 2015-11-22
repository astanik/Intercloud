package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.PropertyModel;

public class AttributeFloatInput extends AttributeNumberInput {
    public AttributeFloatInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new NumberTextField<Float>("attributeValue", new PropertyModel<>(attribute, "float"))
                .setRequired(attribute.isRequired()));
    }
}
