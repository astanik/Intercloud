package de.tu_berlin.cit.intercloud.webapp.template;

import de.agilecoders.wicket.core.markup.html.bootstrap.heading.Heading;
import org.apache.wicket.markup.html.panel.Panel;

public class HeaderPanel extends Panel {
    public HeaderPanel(String id) {
        super(id);

        add(new Heading("title", "Intercloud SLA Mediator Webapp"));
    }
}
