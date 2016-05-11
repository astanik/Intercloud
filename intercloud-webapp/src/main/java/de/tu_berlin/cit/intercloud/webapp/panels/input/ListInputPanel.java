package de.tu_berlin.cit.intercloud.webapp.panels.input;

import de.tu_berlin.cit.intercloud.webapp.components.ListTextArea;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.model.IModel;

import java.util.List;

public class ListInputPanel extends TextareaInputPanel<List<String>> {
    private ListTextArea textArea;

    public ListInputPanel(String id, IModel<List<String>> model) {
        super(id, model);
    }

    @Override
    protected ListTextArea initFormComponent(String markupId) {
        this.textArea  = new ListTextArea(markupId, this.getModel());
        textArea.add(new PlaceholderBehavior("item_1; item_2; ..."));
        return this.textArea;
    }

    @Override
    public ListTextArea getFormComponent() {
        return this.textArea;
    }
}
