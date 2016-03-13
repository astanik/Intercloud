package de.tu_berlin.cit.intercloud.webapp;

import de.tu_berlin.cit.intercloud.webapp.model.User;
import de.tu_berlin.cit.intercloud.xmpp.client.service.mock.XmppServiceMock;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.junit.Assert;

import java.net.URISyntaxException;

public final class MockHelper {
    public static final String WEBAPP_ROOT = "src/main/webapp";

    public static void login() {
        try {
            IntercloudWebSession.get().initializeTestWebSession(new User("hans", Roles.USER), new XmppServiceMock());
        } catch (URISyntaxException e) {
            Assert.fail(e.getMessage());
        }
    }

    public static void logout() {
        IntercloudWebSession.get().signOut();
    }

    public static void initialize() {
        WebApplication webApplication = WebApplication.get();
        webApplication.setServletContext(new MockServletContext(WebApplication.get(), WEBAPP_ROOT));
    }
}
