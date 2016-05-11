package de.tu_berlin.cit.intercloud.webapp.panels.input;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;

public class BooleanInputPanel extends AbstractFormComponentPanel<Boolean> {
    private CheckBox checkBox;

    public BooleanInputPanel(String id, IModel<Boolean> model) {
        super(id, model);
        this.add(initFormComponent("booleanInput"));
    }

    @Override
    protected CheckBox initFormComponent(String markupId) {
        this.checkBox = new CheckBox(markupId, this.getModel());
        return this.checkBox;
    }

    @Override
    public CheckBox getFormComponent() {
        return this.checkBox;
    }
}
