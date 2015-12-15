package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import java.util.List;

public class ListTextArea extends TextArea<List<String>> {
    IConverter listConverter = new ListConverter();

    public ListTextArea(String id, IModel<List<String>> model) {
        super(id, model);
    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return List.class.isAssignableFrom(type) ? this.listConverter : super.getConverter(type);
    }
}
