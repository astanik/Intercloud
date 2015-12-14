package de.tu_berlin.cit.intercloud.webapp.panels.request.attribute;

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
        boolean enabled = attribute.isMutable();
        boolean visible = attribute.isMutable() || (!attribute.isMutable() && attribute.hasValue());
        return newInstance(markupId, attribute, enabled, visible);
    }


    public static AbstractAttributeInput newInstance(String markupId, AttributeModel attribute, boolean enabled, boolean visible) {
        AbstractAttributeInput newInstance;
        switch (attribute.getType()) {
            case STRING:
                newInstance = new StringInput(markupId, attribute);
                break;
            case INTEGER:
                newInstance = new IntegerInput(markupId, attribute);
                break;
            case ENUM:
                newInstance = new EnumInput(markupId, attribute);
                break;
            case DOUBLE:
                newInstance = new DoubleInput(markupId, attribute);
                break;
            case FLOAT:
                newInstance = new FloatInput(markupId, attribute);
                break;
            case BOOLEAN:
                newInstance = new BooleanInput(markupId, attribute);
                break;
            case DATETIME:
                newInstance = new DatetimeInput(markupId, attribute);
                break;
            case DURATION:
                newInstance = new DurationInput(markupId, attribute);
                break;
            case URI:
                newInstance = new UriInput(markupId, attribute);
                break;
            case LIST:
                newInstance = new ListInput(markupId, attribute);
                break;
            case MAP:
                newInstance = new MapInput(markupId, attribute);
                break;
            case SIGNATURE:
            case KEY:
            default:
                throw new UnsupportedOperationException("AttributeModel Typ " + attribute.getType() + " is not supported.");
        }
        return (AbstractAttributeInput) newInstance.setEnabled(enabled).setVisible(visible);
    }
}
