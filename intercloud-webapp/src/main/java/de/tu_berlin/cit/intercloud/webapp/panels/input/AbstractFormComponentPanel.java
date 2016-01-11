package de.tu_berlin.cit.intercloud.webapp.panels.input;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class AbstractFormComponentPanel<T> extends Panel {
    private final IModel<T> model;

    public AbstractFormComponentPanel(String id, IModel<T> model) {
        super(id);
        this.model = model;
    }

    public IModel<T> getModel() {
        return this.model;
    }

    public T getModelObject() {
        return this.model.getObject();
    }

    protected abstract FormComponent initFormComponent(String markupId);
    public abstract FormComponent getFormComponent();
}
