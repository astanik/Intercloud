package de.tu_berlin.cit.intercloud.webapp.panels.attribute;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.tu_berlin.cit.intercloud.client.model.occi.Attribute;
import org.apache.wicket.model.PropertyModel;

class DatetimeInput extends TextInput {
    public DatetimeInput(String markupId, Attribute attribute) {
        super(markupId, attribute);
        this.add(new DatetimePicker("attributeValue", new PropertyModel<>(attribute, "datetime"),
                new DatetimePickerConfig().setCollapse(true).setShowClose(true).withFormat("MM-dd-YYYY HH:mm")
        ).setRequired(attribute.isRequired()));
    }
}
