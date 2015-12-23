package de.tu_berlin.cit.intercloud.webapp.panels.input;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePickerConfig;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.model.IModel;

import java.util.Date;

public class DatetimeInputPanel extends TextInputPanel<Date> {
    private static final String FORMAT = "MM-dd-YYYY HH:mm";
    private DatetimePicker datetimePicker;

    public DatetimeInputPanel(String id, IModel<Date> model) {
        super(id, model);
    }

    @Override
    protected DatetimePicker initFormComponent(String markupId) {
        DatetimePickerConfig config = new DatetimePickerConfig();
        config.setCollapse(true)
                .setShowClose(true)
                .withFormat(FORMAT);
        this.datetimePicker = new DatetimePicker(markupId, this.getModel(), config);
        datetimePicker.add(new PlaceholderBehavior(FORMAT));
        return this.datetimePicker;
    }

    @Override
    public DatetimePicker getFormComponent() {
        return this.datetimePicker;
    }
}
