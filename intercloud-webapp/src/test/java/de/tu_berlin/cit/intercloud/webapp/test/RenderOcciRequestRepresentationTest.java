package de.tu_berlin.cit.intercloud.webapp.test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
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

@BenchmarkOptions(benchmarkRounds = 40, warmupRounds = 10)
public class RenderOcciRequestRepresentationTest extends AbstractBenchmark {
    private WicketTester tester;
    private ProfilingService profilingService = ProfilingService.getInstance();

    @Before
    public void setUp() {
        if (null == tester) {
            tester = new WicketTester(new IntercloudWebApplication());
            MockHelper.initialize();
            MockHelper.login();
        }
    }

    @After
    public void tearDown() {
        /*
        MockHelper.logout();
        tester.destroy();
        tester = null;
        */
    }

    @Test
    public void getWebSession() throws Exception {
        XwadlFileConfig xwadlConfig = new XwadlFileConfig(false, 0, 0, 0, 3, 0, true);
        String xwadlPath = XwadlFileBuilder.getInstance().createXwadlFile(xwadlConfig);

        profilingService.setFileName("target/" + xwadlConfig + ".csv");
        profilingService.setFilter("methodTable.methodList.0.methodLink");
        //tester.startPage(new BrowserPage(Model.of(new XmppURI("foo", MockHelper.XWADL_ROOT + "/20mixins.xml"))));
        tester.startPage(new BrowserPage(Model.of(new XmppURI("foo", xwadlPath))));
        tester.assertRenderedPage(BrowserPage.class);

        // test post method
        tester.clickLink("methodTable:methodList:0:methodLink");
        tester.assertRenderedPage(BrowserPage.class);
    }
}
