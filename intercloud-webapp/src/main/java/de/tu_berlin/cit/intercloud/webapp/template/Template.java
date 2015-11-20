package de.tu_berlin.cit.intercloud.webapp.template;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.pages.LoginPage;
import de.tu_berlin.cit.intercloud.webapp.pages.WelcomePage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResourceReference;

public abstract class Template extends WebPage {
    public static String NAVBAR_ID = "navbar";

    public Template() {
        super();

        add(navbar(NAVBAR_ID));
    }

    private Navbar navbar(String markupId) {
        Navbar navbar = new Navbar(markupId);
        navbar.setBrandImage(new ContextRelativeResourceReference("images/intercloud-logo-small.png"), Model.of());

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                new NavbarButton(LoginPage.class, Model.of()).setIconType(GlyphIconType.login)));
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                new NavbarButton(WelcomePage.class, Model.of("Welcome")).setIconType(GlyphIconType.home),
        		new NavbarExternalLink(Model.of("http://citlab.github.io/Intercloud/"))
                .setLabel(Model.of("Documentation"))
                .setTarget(BootstrapExternalLink.Target.blank)
                .setIconType(GlyphIconType.paperclip),
                new NavbarExternalLink(Model.of("https://github.com/citlab/Intercloud"))
                .setLabel(Model.of("Github"))
                .setTarget(BootstrapExternalLink.Target.blank)
                .setIconType(GlyphIconType.export)));

        return navbar;
    }

    public IntercloudWebSession getIntercloudWebSession() {
        return (IntercloudWebSession) this.getSession();
    }
}
