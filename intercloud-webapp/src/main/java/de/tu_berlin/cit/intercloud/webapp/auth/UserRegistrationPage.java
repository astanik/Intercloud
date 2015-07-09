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

package de.tu_berlin.cit.intercloud.webapp.auth;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.openid4java.discovery.DiscoveryInformation;

import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;

/**
 * This class represents the a registration page, which receives the
 * authentication response from the OpenID Provider (OP) and verifies the
 * response with openid4java. It also provides a way to save the information
 * retrieved from the OP in a database and the user in the session.
 * 
 */
@SuppressWarnings("serial")
public class UserRegistrationPage extends AbstractAuthPage {

	/**
	 * Constructor
	 */
	public UserRegistrationPage() {
		this(new PageParameters());
	}

	/**
	 * Constructor called by Wicket with an authentication response.
	 * 
	 * @param pageParameters
	 *            The request parameters that are the response parameters from
	 *            the OP.
	 */
	public UserRegistrationPage(PageParameters pageParameters) {
		super();
		IntercloudWebSession session = (IntercloudWebSession) getSession();
		OpenIdUser user = null;

		if (!pageParameters.isEmpty()) {
			// If this is a return trip (the OP will redirect here once
			// authentication
			// is compelete), then verify the response.
			if (pageParameters.get("is_return").toBoolean()) {
				//
				// Grab the session object so we can let openid4java do
				// verification.
				//
				DiscoveryInformation discoveryInformation = session
						.getDiscoveryInformation();
				//
				// Delegate to the Service object to do verification. It will
				// return
				// / the RegistrationModel to use to display the information
				// that was
				// / retrieved from the OP about the User-Supplied identifier.
				// The
				// / RegistrationModel reference will be null if there was a
				// problem
				// / (check the logs for more information if this happens).
				//
				user = OpenIdService.processReturn(discoveryInformation,
						pageParameters, OpenIdService.getReturnToUrl());
				if (user == null) {
					//
					// Oops, something went wrong. Display a message on the
					// screen.
					// / Check the logs for more information.
					//
					error("Open ID Confirmation Failed. No information was retrieved from the OpenID Provider.");
				}
			} else {
				error("Corrupt page call. No information was retrieved from the OpenID Provider.");
			}
		} else {
			error("Open ID Confirmation Failed. No page parameters were retrieved from the OpenID Provider.");
		}

		if (user.getFullName().isEmpty() || user.getEmailAddress().isEmpty()) {
			error("Open ID Confirmation Failed. Required information were not retrieved from the OpenID Provider.");
		} 
		
		// Redirect to the webapp
		if(session.authenticate(user))
			throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
//		throw new ImmediateRedirectException(getApplication().getHomePage());
	}

}
