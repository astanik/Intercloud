package de.tu_berlin.cit.intercloud.webapp.test;

import de.tu_berlin.cit.intercloud.client.profiling.FileListener;
import de.tu_berlin.cit.intercloud.client.profiling.ProfilingService;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebApplication;
import de.tu_berlin.cit.intercloud.webapp.MockHelper;
import de.tu_berlin.cit.intercloud.webapp.XwadlFileBuilder;
import de.tu_berlin.cit.intercloud.webapp.XwadlFileConfig;
import de.tu_berlin.cit.intercloud.webapp.pages.BrowserPage;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RenderOcciRequestRepresentationTest {
    private static final int WARMUP_ROUNDS = 10;
    private static final int TEST_ROUNDS = 40;
    private WicketTester tester;
    private ProfilingService profilingService = ProfilingService.getInstance();

    @Before
    public void setUp() {
        tester = new WicketTester(new IntercloudWebApplication());
        MockHelper.initialize();
        MockHelper.login();
    }

    @After
    public void tearDown() {
        MockHelper.logout();
        tester.destroy();
        tester = null;
        profilingService.setFilter(null);
    }

    @Test
    public void tenCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 10, 0, false));
    }

    @Test
    public void twentyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 20, 0, false));
    }

    @Test
    public void thrityCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 30, 0, false));
    }

    @Test
    public void fourtyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 40, 0, false));
    }

    @Test
    public void fiftyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 50, 0, false));
    }

    @Test
    public void sixtyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 60, 0, false));
    }

    @Test
    public void seventyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 70, 0, false));
    }

    @Test
    public void eightyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 80, 0, false));
    }

    @Test
    public void ninetyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 90, 0, false));
    }

    @Test
    public void hundredCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 100, 0, false));
    }

    public void testBrowserPage(XwadlFileConfig xwadlConfig) throws Exception {
        testBrowserPage(xwadlConfig, WARMUP_ROUNDS);
        //ListListener listener = new ListListener();
        profilingService.setProfilingListener(new FileListener("target/" + xwadlConfig + ".csv"));
        //profilingService.setProfilingListener(listener);
        profilingService.setFilter("methodTable.methodList.0.methodLink");
        testBrowserPage(xwadlConfig, TEST_ROUNDS);
    }

    public void testBrowserPage(XwadlFileConfig xwadlConfig, int rounds) throws Exception {
        for (int i = 0; i < rounds; i++) {
            String xwadlPath = XwadlFileBuilder.getInstance().createXwadlFile(xwadlConfig);

            //tester.startPage(new BrowserPage(Model.of(new XmppURI("foo", MockHelper.XWADL_ROOT + "/20mixins.xml"))));
            tester.startPage(new BrowserPage(Model.of(new XmppURI("foo", xwadlPath))));
            tester.assertRenderedPage(BrowserPage.class);

            // test post method
            tester.clickLink("methodTable:methodList:0:methodLink");
            tester.assertRenderedPage(BrowserPage.class);
        }
    }
}
