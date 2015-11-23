package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;

abstract class NumberInput extends AbstractAttributeInput {
    public NumberInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
    }
}
