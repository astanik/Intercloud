package de.tu_berlin.cit.intercloud.webapp.panels.input;

import org.apache.wicket.model.IModel;

public abstract class TextareaInputPanel<T> extends AbstractFormComponentPanel<T> {
    public TextareaInputPanel(String id, IModel<T> model) {
        super(id, model);
        this.add(initFormComponent("textareaInput"));
    }
}
