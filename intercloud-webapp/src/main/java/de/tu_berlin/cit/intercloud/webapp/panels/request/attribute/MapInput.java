package de.tu_berlin.cit.intercloud.webapp.panels.request.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.components.MapTextArea;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.PropertyModel;

class MapInput extends TextareaInput {
    public MapInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
    }

    @Override
    public FormComponent getInputFormComponent() {
        MapTextArea textArea = new MapTextArea("attributeValue", new PropertyModel<>(getAttribute(), "map"));
        textArea.add(new PlaceholderBehavior("key_1=val_1; key_2=val_2; ..."));
        return textArea;
    }
}
