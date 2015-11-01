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

import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.layout.Index;
import de.tu_berlin.cit.intercloud.webapp.xmpp.XmppService;
import de.tu_berlin.cit.intercloud.webapp.xmpp.XmppUser;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginPage extends AbstractAuthPage {
	private final static Logger logger = LoggerFactory.getLogger(LoginForm.class);

	/**
	 * Constructor
	 */
	public LoginPage() {
		super();

		// add form
		add(new LoginForm(this));
	}

	public class LoginForm extends Form<XmppUser> {
		private XmppUser modelObject;

		public LoginForm(final LoginPage loginPage) {

			super("loginForm", new CompoundPropertyModel(XmppService.getInstance().generateXmppUser()));
			modelObject = getModelObject();

			this.add(new TextField("username", new PropertyModel<String>(modelObject, "username")).setRequired(true));
			this.add(new PasswordTextField("password", new PropertyModel(modelObject, "password")).setRequired(true));
			this.add(new TextField("serviceName", new PropertyModel<String>(modelObject, "serviceName")).setRequired(true));
			this.add(new TextField("host", new PropertyModel<String>(modelObject, "host")).setRequired(true));
			this.add(new NumberTextField("port", new PropertyModel<Integer>(modelObject, "port")).setRequired(true));
		}

		@Override
		protected void onSubmit() {
			XmppUser modelObject = this.getModelObject();
			IntercloudWebSession session = (IntercloudWebSession) this.getSession();

			try {
				AbstractXMPPConnection connection = XmppService.getInstance().connect(modelObject);
				session.authenticate(modelObject, connection);
			} catch (Exception e) {
				error("Failed to connect to XMPP server.");
				logger.error("Failed to connect to XMPP server.", e);
			}

			if (session.isSignedIn()) {
				getRequestCycle().setResponsePage(Index.class);
			}
		}
	}

}
