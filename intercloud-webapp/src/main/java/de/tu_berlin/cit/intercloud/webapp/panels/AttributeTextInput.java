package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;

public abstract class AttributeTextInput extends AbstractAttributeInput {
    public AttributeTextInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
    }
}
