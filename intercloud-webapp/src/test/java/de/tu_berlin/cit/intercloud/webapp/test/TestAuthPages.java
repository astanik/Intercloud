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

package de.tu_berlin.cit.intercloud.webapp.test;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import de.tu_berlin.cit.intercloud.webapp.IntercloudWebApplication;
import de.tu_berlin.cit.intercloud.webapp.auth.OpenIdLoginPage;
import de.tu_berlin.cit.intercloud.webapp.auth.UserRegistrationPage;

/**
 * Simple test using the WicketTester
 */
public class TestAuthPages
{
	private WicketTester tester;

	@Before
	public void setUp()
	{
		tester = new WicketTester(new IntercloudWebApplication());
	}


	@Test
	public void LoginPageRendersSuccessfully()
	{
		//start and render the test page
		tester.startPage(OpenIdLoginPage.class);

		//assert rendered page class
		tester.assertRenderedPage(OpenIdLoginPage.class);
	}

	@Test
	public void UserRegistrationPageRendersSuccessfully()
	{
		//start and render the test page
		//tester.startPage(UserRegistrationPage.class);

		//assert rendered page class
		//tester.assertRenderedPage(UserRegistrationPage.class);
	}

}
