package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import java.util.Map;

/**
 * A TextArea for a {@link Map<String, String>}.
 */
public class MapTextArea extends TextArea<Map<String, String>> {
    IConverter mapConverter = new MapConverter();

    public MapTextArea(String id, IModel<Map<String, String>> model) {
        super(id, model);
    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return Map.class.isAssignableFrom(type) ? this.mapConverter : super.getConverter(type);
    }
}
