package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.model.Model;

/**
 * Utility class for {@link Component}s.
 */
public class ComponentUtils {
    /**
     * Hides a component by setting its css style to "display:none".
     * @param component
     * @param <T>
     * @return
     */
    public static  <T extends Component> T displayNone(T component) {
        component.add(new AttributeModifier("style", new Model("display:none")));
        return component;
    }

    /**
     * Displays a component by setting its css style to "display:block".
     * @param component
     * @param <T>
     * @return
     */
    public static <T extends Component> T displayBlock(T component) {
        component.add(new AttributeModifier("style", new Model("display:block")));
        return component;
    }
}
