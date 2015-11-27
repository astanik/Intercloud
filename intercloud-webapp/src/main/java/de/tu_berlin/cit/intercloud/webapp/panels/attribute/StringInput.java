package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

class StringInput extends TextInput {
    public StringInput(String markupId, AttributeModel attribute, boolean enabled) {
        super(markupId, attribute, enabled);
        this.add(new TextField<String>("attributeValue", new PropertyModel<>(attribute, "string"))
                .setRequired(attribute.isRequired()).setEnabled(enabled));
    }
}
