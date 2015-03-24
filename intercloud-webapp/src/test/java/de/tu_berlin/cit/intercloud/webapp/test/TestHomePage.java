package de.tu_berlin.cit.intercloud.webapp.test;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import de.tu_berlin.cit.intercloud.webapp.IntercloudWebApplication;
import de.tu_berlin.cit.intercloud.webapp.layout.BasePage;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage
{
	private WicketTester tester;

	@Before
	public void setUp()
	{
		tester = new WicketTester(new IntercloudWebApplication());
	}

	@Test
	public void indexRendersSuccessfully()
	{
		//start and render the test page
		tester.startPage(BasePage.class);

		//assert rendered page class
		tester.assertRenderedPage(BasePage.class);
	}
}
