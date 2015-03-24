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

package de.tu_berlin.cit.intercloud.webapp.content;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.PatternValidator;

import de.tu_berlin.cit.intercloud.webapp.ThemisWebSession;
import de.tu_berlin.cit.intercloud.webapp.auth.User;
import de.tu_berlin.cit.intercloud.webapp.layout.BasePage;



/**
 * A web page that displays the credentials (username, roles) of the currently
 * logged in user. Additionally it provides a form to change the password.
 * 
 * @author Alexander Stanik
 * 
 */
@AuthorizeInstantiation({ "USER", "ADMIN" })
@SuppressWarnings("serial")
public class CredentialsPage extends BasePage {

	/**
	 * text field for the current password (for authentication purposes).
	 */
	private PasswordTextField oldPassword;

	/**
	 * text field for the new password that the user makes up.
	 */
	private PasswordTextField newPassword;

	/**
	 * text field for validating that the new password was typed in correctly.
	 */
	private PasswordTextField confirmPassword;

	/**
	 * Default constructor that builds the web page
	 */
	public CredentialsPage() {
		init();
	}

	/**
	 * gets the User of the current web session associated to the page request.
	 * 
	 * @return the user of the current web session.
	 */
	private User getSessionUser() {
		ThemisWebSession currentSession = (ThemisWebSession) ThemisWebSession.get();
		return currentSession.getUser();
	}

	/**
	 * initializes the web page. Builds the credentials display and password
	 * form.
	 */
	private void init() {
		User user = getSessionUser();

		if (user == null) {
			// should actually never happen as long as role based access is
			// working
			info("You are not logged in");
			return;
		}

		// show user details
		add(new Label("username", user.getUserName()));
		add(new Label("userroles", user.getRoles().toString()));

		// password form
/*		Form<?> form = new Form<Void>("passwordForm") {

			@Override
			protected void onSubmit() {
				User user = getSessionUser();

				// check validity of old password
				boolean isOldValid = user.isValidPassword(oldPassword.getModelObject());
				if (isOldValid) {

					// update the password of the user
					User newUser = UserAccounts.getInstance().createUser(user.getName(), newPassword.getModelObject(), user.getRoles().toString(), user.isActivated());
					if(UserAccounts.getInstance().setUser(newUser)) {
						info("Password changed.");
					}
					else {
						info("Password could not be changed");
					}
				} else {
					info("Old Password is incorrect!");
				}
			}
		};
		add(form);

		// old password field
		oldPassword = new PasswordTextField("oldPassword", Model.of(""));
		oldPassword.setRequired(true);
		oldPassword.setLabel(Model.of("Old Password"));
		form.add(oldPassword);

		// new password field
		newPassword = new PasswordTextField("newPassword", Model.of(""));
		//newPassword.add(new PatternValidator(CryptoString.PASSWORD_PATTERN));
		newPassword.setRequired(true);
		newPassword.setLabel(Model.of("New Password"));
		form.add(newPassword);

		// confirm password field
		confirmPassword = new PasswordTextField("confirmPassword", Model.of(""));
		confirmPassword.setRequired(true);
		confirmPassword.setLabel(Model.of("Confirm Password"));
		form.add(confirmPassword);
		form.add(new EqualPasswordInputValidator(newPassword, confirmPassword));
*/
		// add a feedback panel to show errors, etc.
		FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
	}
}