package de.tu_berlin.cit.intercloud.webapp.template;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

/**
 * This Template page is restricted to logged in users.
 */
@AuthorizeInstantiation(Roles.USER)
public abstract class UserTemplate extends Template {

    public UserTemplate() {
        super();
    }
}
