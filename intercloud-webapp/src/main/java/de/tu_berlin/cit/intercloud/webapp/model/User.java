package de.tu_berlin.cit.intercloud.webapp.model;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;

public class User implements IUser {
    private String username;
    private Roles roles;

    public User(String username, String roles) {
        this.username = username;
        this.roles = new Roles(roles);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
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
