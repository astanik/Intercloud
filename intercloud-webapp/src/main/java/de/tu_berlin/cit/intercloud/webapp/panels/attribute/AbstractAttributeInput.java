package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public abstract class AbstractAttributeInput extends Panel {
    public AbstractAttributeInput(String markupId, AttributeModel attribute, boolean enabled) {
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
        boolean enabled = attribute.isMutable();
        boolean visible = attribute.isMutable() || (!attribute.isMutable() && attribute.hasValue());
        return newInstance(markupId, attribute, enabled, visible);
    }


    public static AbstractAttributeInput newInstance(String markupId, AttributeModel attribute, boolean enabled, boolean visible) {
        switch (attribute.getType()) {
            case STRING:
                return (AbstractAttributeInput) new StringInput(markupId, attribute, enabled).setVisible(visible);
            case INTEGER:
                return (AbstractAttributeInput) new IntegerInput(markupId, attribute, enabled).setVisible(visible);
            case ENUM:
                return (AbstractAttributeInput) new EnumInput(markupId, attribute, enabled).setVisible(visible);
            case DOUBLE:
                return (AbstractAttributeInput) new DoubleInput(markupId, attribute, enabled).setVisible(visible);
            case FLOAT:
                return (AbstractAttributeInput) new FloatInput(markupId, attribute, enabled).setVisible(visible);
            case BOOLEAN:
                return (AbstractAttributeInput) new BooleanInput(markupId, attribute, enabled).setVisible(visible);
            case DATETIME:
                return (AbstractAttributeInput) new DatetimeInput(markupId, attribute, enabled).setVisible(visible);
            case URI:
                return (AbstractAttributeInput) new UriInput(markupId, attribute, enabled).setVisible(visible);
            case SIGNATURE:
            case KEY:
            case DURATION:
            default:
                throw new UnsupportedOperationException("AttributeModel Typ " + attribute.getType() + " is not supported.");
        }
    }
}
