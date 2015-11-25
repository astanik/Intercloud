package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.Attribute;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

class UriInput extends TextInput {
    public UriInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new TextField<String>("attributeValue", new PropertyModel<>(attribute, "uri"))
                .setRequired(attribute.isRequired()));
        // TODO xmpp uri validation???
    }
}
