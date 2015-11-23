package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public abstract class AbstractAttributeInput extends Panel {
    public AbstractAttributeInput(String markupId, Attribute attribute) {
        super(markupId);
        this.add(new Label("attributeName", Model.of(attribute.getName() + (attribute.isRequired() ? "*" : ""))));
        Model description = null == attribute.getDescription() ? Model.of() : Model.of(attribute.getDescription());
        this.add(new Label("attributeDescription", description));
    }

    public static AbstractAttributeInput newInstance(String markupId, Attribute attribute) {
        switch (attribute.getType()) {
            case STRING:
                return new AttributeStringInput(markupId, attribute);
            case INTEGER:
                return new AttributeIntegerInput(markupId, attribute);
            case ENUM:
                return new AttributeEnumInput(markupId, attribute);
            case DOUBLE:
                return new AttributeDoubleInput(markupId, attribute);
            case FLOAT:
                return new AttributeFloatInput(markupId, attribute);
            case BOOLEAN:
                return new AttributeBooleanInput(markupId, attribute);
            case DATETIME:
                return new AttributeDatetimeInput(markupId, attribute);
            case URI:
                return new AttributeUriInput(markupId, attribute);
            case SIGNATURE:
            case KEY:
            case DURATION:
            default:
                throw new UnsupportedOperationException("Attribute Typ " + attribute.getType() + " is not supported.");
        }
    }
}
