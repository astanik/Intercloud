package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.PropertyModel;

public class AttributeBooleanInput extends AbstractAttributeInput {
    public AttributeBooleanInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new CheckBox("attributeValue", new PropertyModel<>(attribute, "boolean"))
                .setRequired(attribute.isRequired()));
    }
}
