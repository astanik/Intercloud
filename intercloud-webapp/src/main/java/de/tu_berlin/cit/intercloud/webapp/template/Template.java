package de.tu_berlin.cit.intercloud.webapp.template;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.tu_berlin.cit.intercloud.webapp.pages.LoginPage;
import de.tu_berlin.cit.intercloud.webapp.pages.WelcomePage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

public abstract class Template extends WebPage {
    public static String NAVBAR_ID = "navbar";

    public Template() {
        super();

        add(navbar(NAVBAR_ID));
    }

    private Navbar navbar(String markupId) {
        Navbar navbar = new Navbar(markupId);
        navbar.setBrandName(Model.of("Intercloud"));

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                new NavbarButton(LoginPage.class, Model.of()).setIconType(GlyphIconType.login)));
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                new NavbarButton(WelcomePage.class, Model.of("Welcome")).setIconType(GlyphIconType.home)));

        return navbar;
    }
}
