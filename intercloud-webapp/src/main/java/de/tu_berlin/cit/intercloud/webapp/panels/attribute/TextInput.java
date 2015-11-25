package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.Attribute;

abstract class TextInput extends AbstractAttributeInput {
    public TextInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
    }
}
