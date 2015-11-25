package de.tu_berlin.cit.intercloud.webapp.template;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.pages.DiscoverItemsPage;
import de.tu_berlin.cit.intercloud.webapp.pages.LoginPage;
import de.tu_berlin.cit.intercloud.webapp.pages.WelcomePage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResourceReference;

import java.util.ArrayList;
import java.util.List;

public abstract class Template extends WebPage {

    public Template() {
        super();

        add(navbar("navbar", IntercloudWebSession.get()));
    }

    private Navbar navbar(String markupId, IntercloudWebSession session) {
        Navbar navbar = new Navbar(markupId);
        navbar.setBrandImage(new ContextRelativeResourceReference("images/intercloud-logo-small.png", false), Model.of());

        if (session.isSignedIn()) {
            navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                    new NavbarDropDownButton(Model.of()) {
                        @Override
                        protected List<AbstractLink> newSubMenuButtons(String s) {
                            final List<AbstractLink> subMenu = new ArrayList<>();
                            subMenu.add(new MenuHeader(Model.of(session.getUser().getUsername())));
                            subMenu.add(new Link(s) {
                                @Override
                                public void onClick() {
                                    session.signOut();
                                    setResponsePage(WelcomePage.class);
                                }
                            }.setBody(Model.of("Logout")));
                            return subMenu;
                        }
                    }.setIconType(GlyphIconType.user))
            );
            navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                    new NavbarButton(DiscoverItemsPage.class, Model.of("Discover")).setIconType(GlyphIconType.zoomin)));
        } else {
            navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                    new NavbarButton(LoginPage.class, Model.of("Login")).setIconType(GlyphIconType.login)));
            navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                    new NavbarButton(WelcomePage.class, Model.of("Welcome")).setIconType(GlyphIconType.home)));
        }

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
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
}
