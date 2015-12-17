package de.tu_berlin.cit.intercloud.webapp.panels.request.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.components.ListTextArea;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.PropertyModel;

class ListInput extends TextareaInput {
    public ListInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
    }

    @Override
    public FormComponent getInputFormComponent() {
        ListTextArea textArea  = new ListTextArea("attributeValue", new PropertyModel<>(getAttribute(), "list"));
        textArea.add(new PlaceholderBehavior("item_1; item_2; ..."));
        return textArea;
    }
}
