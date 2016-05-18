package de.tu_berlin.cit.intercloud.webapp.panels.method.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.components.AjaxFormComponentValidationBehavior;
import de.tu_berlin.cit.intercloud.webapp.panels.input.AbstractFormComponentPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.BooleanInputPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.DatetimeInputPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.DurationInputPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.ListInputPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.MapInputPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.NumberInputPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.StringInputPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Displays an {@link AttributeModel} for user input.
 * It displays the {@link AttributeModel#name} and {@link AttributeModel#description}
 * as labels.
 * It displays the {@link AttributeModel#value} as an input field, while
 * it uses the {@link AbstractFormComponentPanel} to do so.
 */
public class AttributeInputPanel extends Panel {
    private final Label nameLabel;
    private final IModel<AttributeModel> attributeModel;

    public AttributeInputPanel(String markupId, IModel<AttributeModel> attributeModel) {
        super(markupId);
        this.attributeModel = attributeModel;

        AttributeModel attribute = attributeModel.getObject();
        // attribute name
        this.nameLabel = new Label("attributeName", Model.of(attribute.getName() + (attribute.isRequired() ? "*" : "")));
        this.nameLabel.setOutputMarkupId(true);
        this.add(this.nameLabel);

        // attribute description
        Label descriptionLabel = new Label("attributeDescription");
        if (null == attribute.getDescription() || attribute.getDescription().trim().isEmpty()) {
            descriptionLabel.setVisible(false);
        } else {
            descriptionLabel.setDefaultModel(Model.of(attribute.getDescription()));
        }
        this.add(descriptionLabel);

        // attribute input
        AbstractFormComponentPanel inputPanel = newInputPanel("inputPanel", attribute);
        this.add(inputPanel);
        FormComponent inputFormComponent = inputPanel.getFormComponent();
        inputFormComponent.setRequired(attribute.isRequired());
        inputFormComponent.add(new AjaxFormComponentValidationBehavior("blur", this.nameLabel));
        if (attribute.isRequired() && !attribute.hasValue()) {
            this.nameLabel.add(new AjaxFormComponentValidationBehavior.InvalidAttributeModifier());
        }
    }

    @Override
    public boolean isVisible() {
        AttributeModel attribute = this.attributeModel.getObject();
        return attribute.isMutable() || (!attribute.isMutable() && attribute.hasValue());
    }

    /**
     * Creates an input form component for an {@link AttributeModel}
     * depending on the {@link AttributeModel#type}.
     * @param markupId
     * @param attribute
     * @return
     */
    public AbstractFormComponentPanel newInputPanel(String markupId, AttributeModel attribute) {
        AbstractFormComponentPanel formComponent;
        switch (attribute.getType()) {
            case STRING:
                formComponent = new StringInputPanel(markupId, new PropertyModel<>(attribute, "string"));
                break;
            case ENUM:
                formComponent = new StringInputPanel(markupId, new PropertyModel<>(attribute, "enum"));
                break;
            case URI:
                formComponent = new StringInputPanel(markupId, new PropertyModel<>(attribute, "uri"));
                break;
            case SIGNATURE:
                formComponent = new StringInputPanel(markupId, new PropertyModel<>(attribute, "signature"));
                break;
            case KEY:
                formComponent = new StringInputPanel(markupId, new PropertyModel<>(attribute, "key"));
                break;
            case INTEGER:
                formComponent = new NumberInputPanel<Integer>(markupId, new PropertyModel<>(attribute, "integer"));
                break;
            case DOUBLE:
                formComponent = new NumberInputPanel<Double>(markupId, new PropertyModel<>(attribute, "double"));
                break;
            case FLOAT:
                formComponent = new NumberInputPanel<Float>(markupId, new PropertyModel<>(attribute, "float"));
                break;
            case BOOLEAN:
                formComponent = new BooleanInputPanel(markupId, new PropertyModel<>(attribute, "boolean"));
                break;
            case DATETIME:
                formComponent = new DatetimeInputPanel(markupId, new PropertyModel<>(attribute, "datetime"));
                break;
            case DURATION:
                formComponent = new DurationInputPanel(markupId, new PropertyModel<>(attribute, "duration"));
                break;
            case LIST:
                formComponent = new ListInputPanel(markupId, new PropertyModel<>(attribute, "list"));
                break;
            case MAP:
                formComponent = new MapInputPanel(markupId, new PropertyModel<>(attribute, "map"));
                break;
            default:
                throw new UnsupportedOperationException("AttributeModel Typ " + attribute.getType() + " is not supported.");
        }
        return (AbstractFormComponentPanel) formComponent.setEnabled(attribute.isMutable());
    }
}
