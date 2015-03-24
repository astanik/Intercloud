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
