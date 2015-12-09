package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.components.MapTextArea;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.model.PropertyModel;

class MapInput extends TextareaInput {
    public MapInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
        this.add(new MapTextArea("attributeValue", new PropertyModel<>(attribute, "map"))
                .setRequired(attribute.isRequired()).add(new PlaceholderBehavior("key_1=val_1; key_2=val_2; ...")));
    }
}
