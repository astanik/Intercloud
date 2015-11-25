package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.Attribute;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

class EnumInput extends TextInput {
    public EnumInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new TextField<String>("attributeValue", new PropertyModel<>(attribute, "enum"))
                .setRequired(attribute.isRequired()));
    }
}
