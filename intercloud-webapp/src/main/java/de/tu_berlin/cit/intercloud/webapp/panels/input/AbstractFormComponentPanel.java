package de.tu_berlin.cit.intercloud.webapp.panels.input;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This panel acts as a container for a {@link FormComponent}, e.g.
 * {@link org.apache.wicket.markup.html.form.TextField} or
 * {@link org.apache.wicket.markup.html.form.CheckBox}.
 * It enables to create type-safe {@link FormComponent}s at runtime.
 * @param <T> The type of data.
 */
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

    /**
     * Initialize the {@link FormComponent}.
     * @param markupId
     * @return
     */
    protected abstract FormComponent initFormComponent(String markupId);

    /**
     * Returns the {@link FormComponent} used in this container.
     * @return
     */
    public abstract FormComponent getFormComponent();
}
