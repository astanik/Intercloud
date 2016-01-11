package de.tu_berlin.cit.intercloud.webapp.panels.input;

import de.tu_berlin.cit.intercloud.webapp.components.DurationTextField;
import de.tu_berlin.cit.intercloud.webapp.components.PlaceholderBehavior;
import org.apache.wicket.model.IModel;

import java.time.Duration;

public class DurationInputPanel extends TextInputPanel<Duration> {
    private DurationTextField textField;

    public DurationInputPanel(String id, IModel<Duration> model) {
        super(id, model);
    }

    @Override
    protected DurationTextField initFormComponent(String markupId) {
        this.textField = new DurationTextField(markupId, this.getModel());
        textField.add(new PlaceholderBehavior("PnDTnHnMn"));
        return textField;
    }

    @Override
    public DurationTextField getFormComponent() {
        return this.textField;
    }
}
