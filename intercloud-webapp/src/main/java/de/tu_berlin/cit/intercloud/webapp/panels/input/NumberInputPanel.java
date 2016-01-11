package de.tu_berlin.cit.intercloud.webapp.panels.input;

import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;

public class NumberInputPanel<T extends Number & Comparable<T>> extends AbstractFormComponentPanel<T> {
    private NumberTextField<T> textField;

    public NumberInputPanel(String id, IModel<T> model) {
        super(id, model);
        this.add(initFormComponent("numberInput"));
    }

    @Override
    protected NumberTextField<T> initFormComponent(String markupId) {
        this.textField = new NumberTextField<>(markupId, this.getModel());
        return this.textField;
    }

    @Override
    public NumberTextField<T> getFormComponent() {
        return this.textField;
    }
}
