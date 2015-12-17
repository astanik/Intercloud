package de.tu_berlin.cit.intercloud.webapp.panels.request.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.PropertyModel;

class BooleanInput extends AbstractAttributeInput {
    public BooleanInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
    }

    @Override
    public FormComponent getInputFormComponent() {
        return new CheckBox("attributeValue", new PropertyModel<>(getAttribute(), "boolean"));
    }
}
