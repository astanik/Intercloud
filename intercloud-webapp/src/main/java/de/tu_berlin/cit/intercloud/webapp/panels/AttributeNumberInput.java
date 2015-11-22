package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;

public abstract class AttributeNumberInput extends AbstractAttributeInput {
    public AttributeNumberInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
    }
}
