package de.tu_berlin.cit.intercloud.webapp.xmpp;

import de.tu_berlin.cit.intercloud.webapp.auth.User;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;

public class XmppUser implements User {
    private String username;
    private String password;
    private String serviceName;
    private String host;
    private int port = 5222;

    private Roles roles = new Roles(Roles.ADMIN);

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
