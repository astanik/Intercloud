package de.tu_berlin.cit.intercloud.webapp.panels.input;

import org.apache.wicket.model.IModel;

public abstract class TextInputPanel<T> extends AbstractFormComponentPanel<T> {
    public TextInputPanel(String id, IModel<T> model) {
        super(id, model);
        this.add(initFormComponent("textInput"));
    }
}
