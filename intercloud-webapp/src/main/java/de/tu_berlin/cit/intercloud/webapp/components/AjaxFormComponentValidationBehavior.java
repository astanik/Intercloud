package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;

import java.util.Set;

public class AjaxFormComponentValidationBehavior extends AjaxFormComponentUpdatingBehavior {
    private final Label label;
    private ValidAttributeModifier valid = new ValidAttributeModifier();
    private InvalidAttributeModifier invalid = new InvalidAttributeModifier();

    public AjaxFormComponentValidationBehavior(String event, Label label) {
        super(event);
        this.label = label;
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        target.add(this.label.add(this.valid));
    }

    @Override
    protected void onError(AjaxRequestTarget target, RuntimeException e) {
        target.add(this.label.add(this.invalid));
    }

    public static class ValidAttributeModifier extends ClassAttributeModifier {
        @Override
        protected Set<String> update(Set<String> set) {
            set.remove("invalid");
            return set;
        }
    }

    public static class InvalidAttributeModifier extends ClassAttributeModifier {
        @Override
        protected Set<String> update(Set<String> set) {
            set.add("invalid");
            return set;
        }
    }
}
