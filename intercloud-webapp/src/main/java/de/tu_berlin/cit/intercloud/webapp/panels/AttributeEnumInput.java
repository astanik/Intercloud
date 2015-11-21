package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

public class AttributeEnumInput extends AbstractAttributeInput {
    public AttributeEnumInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new TextField<String>("attributeValue", new PropertyModel<>(attribute, "enum"))
                .setRequired(attribute.isRequired()));
    }
}
