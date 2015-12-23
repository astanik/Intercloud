package de.tu_berlin.cit.intercloud.webapp.panels.input;

import de.tu_berlin.cit.intercloud.webapp.components.MapTextArea;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.model.IModel;

import java.util.Map;

public class MapInputPanel extends TextareaInputPanel<Map<String, String>> {
    private MapTextArea textArea;

    public MapInputPanel(String id, IModel<Map<String, String>> model) {
        super(id, model);
    }

    @Override
    protected MapTextArea initFormComponent(String markupId) {
        this.textArea = new MapTextArea(markupId, this.getModel());
        textArea.add(new PlaceholderBehavior("key_1=val_1; key_2=val_2; ..."));
        return this.textArea;
    }

    @Override
    public MapTextArea getFormComponent() {
        return this.textArea;
    }
}
