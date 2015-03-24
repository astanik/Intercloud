/**
 * Copyright (C) 2012-2015 TU Berlin. All rights reserved.
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

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;

import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;

/**
 * Page that allows users to login into the website with their Open Id account.
 * 
 * @author Alexander Stanik
 */
@SuppressWarnings("serial")
public class OpenIdLoginPage extends AbstractAuthPage {

	/**
	 * Constructor
	 */
	public OpenIdLoginPage() {
		super();

		// add form
		add(new OpenIdRegistrationForm(this));
	}

	/**
	 * The Open ID login form used for this Page.
	 * 
	 */
	public class OpenIdRegistrationForm extends Form<OpenIdUser> {

		public OpenIdRegistrationForm(final OpenIdLoginPage loginPage) {

			super("form", new CompoundPropertyModel<OpenIdUser>(
					new OpenIdUser()));
			//

			final OpenIdUser formModel = (OpenIdUser) this
					.getDefaultModelObject();

			// setModelObject(formModel);
			//
			add(new TextField<String>("openId").setRequired(true).setLabel(
					new Model<String>("Your Open ID")));
			// This is the "business end" of making the authentication request.
			Button confirmOpenIdButton = new Button("confirmOpenIdButton") {
				@Override
				public void onSubmit() {
					// Delegate to Open ID code
					DiscoveryInformation discoveryInformation = OpenIdService
							.performDiscoveryOnUserSuppliedIdentifier(formModel
									.getOpenId());
					// Store the discovery results in session.
					IntercloudWebSession session = (IntercloudWebSession) this
							.getSession();
					session.setDiscoveryInformation(discoveryInformation);
					// Create the AuthRequest
					AuthRequest authRequest = OpenIdService
							.createOpenIdAuthRequest(discoveryInformation,
									OpenIdService.getReturnToUrl());
					// Now take the AuthRequest and forward it on to the OP
					RedirectRequestHandler handler = new RedirectRequestHandler(
							authRequest.getDestinationUrl(true));
					this.getRequestCycle().scheduleRequestHandlerAfterCurrent(
							handler);
				}
			};
			add(confirmOpenIdButton);
		}
	}

}
