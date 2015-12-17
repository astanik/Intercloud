package de.tu_berlin.cit.intercloud.webapp.panels.request.attribute;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.PropertyModel;

class DatetimeInput extends TextInput {
    private static final String FORMAT = "MM-dd-YYYY HH:mm";

    public DatetimeInput(String markupId, AttributeModel attribute) {
        super(markupId, attribute);
    }

    @Override
    public FormComponent getInputFormComponent() {
        DatetimePickerConfig config = new DatetimePickerConfig();
        config.setCollapse(true)
                .setShowClose(true)
                .withFormat(FORMAT);
        DatetimePicker datetimePicker = new DatetimePicker("attributeValue", new PropertyModel<>(getAttribute(), "datetime"), config);
        datetimePicker.add(new PlaceholderBehavior(FORMAT));
        return datetimePicker;
    }
}
