package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

public class PlaceholderBehavior extends Behavior {
    private final String placeholder;

    public PlaceholderBehavior(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        tag.put("placeholder", this.placeholder);
    }
}
