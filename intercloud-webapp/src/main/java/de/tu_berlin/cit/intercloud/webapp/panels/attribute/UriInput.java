package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

class UriInput extends TextInput {
    public UriInput(String markupId, AttributeModel attribute, boolean enabled) {
        super(markupId, attribute, enabled);
        this.add(new TextField<String>("attributeValue", new PropertyModel<>(attribute, "uri"))
                .setRequired(attribute.isRequired()).setEnabled(enabled));
        // TODO xmpp uri validation???
    }
}
