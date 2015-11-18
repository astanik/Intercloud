package de.tu_berlin.cit.intercloud.webapp;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.model.Model;

public class ComponentUtils {
    public static  <T extends Component> T displayNone(T component) {
        component.add(new AttributeModifier("style", new Model("display:none")));
        return component;
    }

    public static <T extends Component> T displayBlock(T component) {
        component.add(new AttributeModifier("style", new Model("display:block")));
        return component;
    }
}
