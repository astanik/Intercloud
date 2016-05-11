package de.tu_berlin.cit.intercloud.webapp.panels.input;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class StringInputPanel extends TextInputPanel<String> {
    private TextField<String> textField;

    public StringInputPanel(String id, IModel<String> model) {
        super(id, model);
    }

    @Override
    protected TextField<String> initFormComponent(String markupId) {
        this.textField = new TextField<>(markupId, this.getModel());
        return this.textField;
    }

    @Override
    public TextField<String> getFormComponent() {
        return textField;
    }
}
