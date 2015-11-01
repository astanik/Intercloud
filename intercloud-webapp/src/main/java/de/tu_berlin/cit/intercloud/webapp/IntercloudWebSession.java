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

import de.tu_berlin.cit.intercloud.webapp.xmpp.XmppUser;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import de.tu_berlin.cit.intercloud.webapp.auth.User;

import java.io.IOException;

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
	private XmppUser user = null;
	private AbstractXMPPConnection connection;
	private boolean signIn = false;

	public IntercloudWebSession(Request request) {
		super(request);
	}

	public boolean authenticate(XmppUser user, AbstractXMPPConnection connection) {
		signOut();

		if (null != user && null != connection) {
			this.user = user;
			this.connection = connection;
			this.signIn = true;
		}

		return isSignedIn();
	}

	public User getUser() {
		return user;
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn() && user != null) {
			return user.getRoles();
		}
		return null;
	}

	@Override
	public boolean isSignedIn() {
		return signIn;
	}

	public void signOut() {
		if (isSignedIn()) {
			this.signIn = false;
			this.user = null;
			this.connection.disconnect();
			this.connection = null;
		}
	}

}
