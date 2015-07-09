/**
 * Copyright 2010-2015 Complex and Distributed IT Systems, TU Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tu_berlin.cit.intercloud.webapp;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.openid4java.discovery.DiscoveryInformation;

import de.tu_berlin.cit.intercloud.webapp.auth.OpenIdUser;
import de.tu_berlin.cit.intercloud.webapp.auth.User;

/**
 * Session class for user authentication.
 * 
 * @author Alexander Stanik
 * 
 */
@SuppressWarnings("serial")
public class IntercloudWebSession extends AbstractAuthenticatedWebSession {

	/**
	 * User of this web session.
	 */
	private User user = null;

	private boolean isSignedIn = false;

	private DiscoveryInformation discoveryInformation;

	public static final String DISCOVERY_INFORMATION = "openid-disc";

	/**
	 * Construct.
	 * 
	 * @param request
	 *            The current request object
	 */
	public IntercloudWebSession(Request request) {
		super(request);
	}


	/**
	 * Returns the roles of the user.
	 */
	@Override
	public Roles getRoles() {
		if (isSignedIn() && user != null) {
			return user.getRoles();
		}
		return null;
	}

	/**
	 * gets the user that is bound to this session.
	 * 
	 * @return the user of this session.
	 */
	public User getUser() {
		return user;
	}

	@Override
	public boolean isSignedIn() {
		return this.isSignedIn;
	}

	public void setDiscoveryInformation(DiscoveryInformation discoveryInformation) {
		this.discoveryInformation = discoveryInformation;
		setAttribute(DISCOVERY_INFORMATION, discoveryInformation);
	}

	public DiscoveryInformation getDiscoveryInformation() {
		DiscoveryInformation ret = this.discoveryInformation;
		ret = (DiscoveryInformation)getAttribute(DISCOVERY_INFORMATION);
		return ret;
	}


	public final boolean authenticate(OpenIdUser user) {
		// TODO Check db, save user 
		this.user = user;
		this.user.setRoles(new Roles(Roles.ADMIN));
		this.isSignedIn = true;
		
		return this.isSignedIn;
	}


	public void signOut() {
		this.user = null;
		this.isSignedIn = false;
	}

}
