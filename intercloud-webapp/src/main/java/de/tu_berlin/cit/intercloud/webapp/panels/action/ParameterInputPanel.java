package de.tu_berlin.cit.intercloud.webapp.panels.action;

import de.tu_berlin.cit.intercloud.client.model.action.ParameterModel;
import de.tu_berlin.cit.intercloud.webapp.components.AjaxFormComponentValidationBehavior;
import de.tu_berlin.cit.intercloud.webapp.panels.input.AbstractFormComponentPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.BooleanInputPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.NumberInputPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.input.StringInputPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Displays a {@link ParameterModel} for user input.
 * It displays the {@link ParameterModel#name} and {@link ParameterModel#documentation}
 * as labels.
 * It displays the {@link ParameterModel#value} as an input field, while
 * it uses the {@link AbstractFormComponentPanel} to do so.
 */
public class ParameterInputPanel extends Panel {
    private final Label nameLabel;

    public ParameterInputPanel(String markupId, IModel<ParameterModel> parameterModel) {
        super(markupId);

        ParameterModel parameter = parameterModel.getObject();
        // parameter name
        this.nameLabel = new Label("parameterName", Model.of(parameter.getName() + (parameter.isRequired() ? "*" : "")));
        this.nameLabel.setOutputMarkupId(true);
        this.add(this.nameLabel);

        // parameter documentation
        Label descriptionLabel = new Label("parameterDocumentation");
        if (null == parameter.getDocumentation() || parameter.getDocumentation().trim().isEmpty()) {
            descriptionLabel.setVisible(false);
        } else {
            descriptionLabel.setDefaultModel(Model.of(parameter.getDocumentation()));
        }
        this.add(descriptionLabel);

        // parameter input
        AbstractFormComponentPanel inputPanel = newInputPanel("inputPanel", parameter);
        this.add(inputPanel);
        FormComponent inputFormComponent = inputPanel.getFormComponent();
        inputFormComponent.setRequired(parameter.isRequired());
        inputFormComponent.add(new AjaxFormComponentValidationBehavior("blur", this.nameLabel));
        if (parameter.isRequired() && !parameter.hasValue()) {
            this.nameLabel.add(new AjaxFormComponentValidationBehavior.InvalidAttributeModifier());
        }
    }

    /**
     * Creates an input form component for a {@link ParameterModel}
     * depending on the {@link ParameterModel#type}.
     * @param markupId
     * @param parameter
     * @return
     */
    public AbstractFormComponentPanel newInputPanel(String markupId, ParameterModel parameter) {
        AbstractFormComponentPanel formComponent;
        switch (parameter.getType()) {
            case STRING:
                formComponent = new StringInputPanel(markupId, new PropertyModel<>(parameter, "string"));
                break;
            case INTEGER:
                formComponent = new NumberInputPanel<Integer>(markupId, new PropertyModel<>(parameter, "integer"));
                break;
            case DOUBLE:
                formComponent = new NumberInputPanel<Double>(markupId, new PropertyModel<>(parameter, "double"));
                break;
            case BOOLEAN:
                formComponent = new BooleanInputPanel(markupId, new PropertyModel<>(parameter, "boolean"));
                break;
            case LINK:
                formComponent = new StringInputPanel(markupId, new PropertyModel<>(parameter, "link"));
                break;
            default:
                throw new UnsupportedOperationException("ParameterModel Type " + parameter.getType() + " is not supported.");
        }
        return formComponent;
    }
}
