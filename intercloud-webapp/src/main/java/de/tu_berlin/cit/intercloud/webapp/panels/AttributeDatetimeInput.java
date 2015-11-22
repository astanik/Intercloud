package de.tu_berlin.cit.intercloud.webapp.panels;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;
import org.apache.wicket.model.PropertyModel;

public class AttributeDatetimeInput extends AttributeTextInput {
    public AttributeDatetimeInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new DatetimePicker("attributeValue", new PropertyModel<>(attribute, "datetime"),
                new DatetimePickerConfig().setCollapse(true).setShowClose(true).withFormat("MM-dd-YYYY HH:mm")
        ).setRequired(attribute.isRequired()));
    }
}
