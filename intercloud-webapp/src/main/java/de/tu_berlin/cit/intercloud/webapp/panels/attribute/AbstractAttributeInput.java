package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public abstract class AbstractAttributeInput extends Panel {
    public AbstractAttributeInput(String markupId, AttributeModel attribute) {
        super(markupId);
        this.add(new Label("attributeName", Model.of(attribute.getName() + (attribute.isRequired() ? "*" : ""))));

        Label descriptionLabel = new Label("attributeDescription");
        if (null == attribute.getDescription() || attribute.getDescription().trim().isEmpty()) {
            descriptionLabel.setVisible(false);
        } else {
            descriptionLabel.setDefaultModel(Model.of(attribute.getDescription()));
        }
        this.add(descriptionLabel);
    }

    public static AbstractAttributeInput newInstance(String markupId, AttributeModel attribute) {
        switch (attribute.getType()) {
            case STRING:
                return new StringInput(markupId, attribute);
            case INTEGER:
                return new IntegerInput(markupId, attribute);
            case ENUM:
                return new EnumInput(markupId, attribute);
            case DOUBLE:
                return new DoubleInput(markupId, attribute);
            case FLOAT:
                return new FloatInput(markupId, attribute);
            case BOOLEAN:
                return new BooleanInput(markupId, attribute);
            case DATETIME:
                return new DatetimeInput(markupId, attribute);
            case URI:
                return new UriInput(markupId, attribute);
            case SIGNATURE:
            case KEY:
            case DURATION:
            default:
                throw new UnsupportedOperationException("AttributeModel Typ " + attribute.getType() + " is not supported.");
        }
    }
}
