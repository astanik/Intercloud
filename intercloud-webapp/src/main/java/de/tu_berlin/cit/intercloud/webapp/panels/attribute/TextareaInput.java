package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;

abstract class TextareaInput extends AbstractAttributeInput {
    public TextareaInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
    }
}
