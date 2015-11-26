package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;

abstract class TextInput extends AbstractAttributeInput {
    public TextInput(String markupId, AttributeModel attribute, boolean enabled) {
        super(markupId, attribute, enabled);
    }
}
