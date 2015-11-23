package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.PropertyModel;

class BooleanInput extends AbstractAttributeInput {
    public BooleanInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new CheckBox("attributeValue", new PropertyModel<>(attribute, "boolean"))
                .setRequired(attribute.isRequired()));
    }
}
