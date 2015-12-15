package de.tu_berlin.cit.intercloud.webapp.model;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;

import java.net.URISyntaxException;

public class User implements IUser {
    private static final long serialVersionUID = 2215265222348014208L;

    private final XmppURI uri;
    private final String username;
    private Roles roles;

    public User(String username, String roles) throws URISyntaxException {
        this.uri = new XmppURI(username, "");
        this.username = username;
        this.roles = new Roles(roles);
    }

    @Override
    public String getUsername() {
        return username;
    }

    public XmppURI getUri() {
        return uri;
    }

    @Override
    public Roles getRoles() {
        return roles;
    }

    @Override
    public void setRoles(Roles roles) {
        this.roles = roles;
    }
}
