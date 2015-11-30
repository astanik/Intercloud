package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;

abstract class NumberInput extends AbstractAttributeInput {
    public NumberInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
    }
}
