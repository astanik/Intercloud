package de.tu_berlin.cit.intercloud.webapp.panels.request.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.components.DurationTextField;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.model.PropertyModel;

class DurationInput extends TextInput {
    public DurationInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
        this.add(new DurationTextField("attributeValue", new PropertyModel<>(attribute, "duration"))
                .setRequired(attribute.isRequired()).add(new PlaceholderBehavior("PnDTnHnMn")));
    }
}
