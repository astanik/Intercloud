package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import java.time.Duration;

public class DurationTextField extends TextField<Duration> {
    private IConverter durationConverter = new DurationConverter();

    public DurationTextField(String id, IModel<Duration> model) {
        super(id, model);
    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return Duration.class.isAssignableFrom(type) ? this.durationConverter : super.getConverter(type);
    }
}
