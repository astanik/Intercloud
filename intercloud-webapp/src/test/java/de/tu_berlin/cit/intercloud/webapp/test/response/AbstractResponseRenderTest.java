package de.tu_berlin.cit.intercloud.webapp.test.response;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import de.tu_berlin.cit.intercloud.client.profiling.impl.ProfilingService;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebApplication;
import de.tu_berlin.cit.intercloud.webapp.MockHelper;
import de.tu_berlin.cit.intercloud.webapp.XwadlFileBuilder;
import de.tu_berlin.cit.intercloud.webapp.XwadlFileConfig;
import de.tu_berlin.cit.intercloud.webapp.pages.BrowserPage;
import de.tu_berlin.cit.intercloud.webapp.profiling.ListListener;
import de.tu_berlin.cit.intercloud.webapp.profiling.ProfilingUtil;
import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;

@BenchmarkOptions(warmupRounds = 0, benchmarkRounds = 10)
abstract class AbstractResponseRenderTest extends AbstractBenchmark {
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
        profilingService.setListener(null);
    }

    public abstract void test1() throws Exception;

    public abstract void test10() throws Exception;

    public abstract void test20() throws Exception;

    public abstract void test30() throws Exception;

    public abstract void test40() throws Exception;

    public abstract void test50() throws Exception;

    public abstract void test60() throws Exception;

    public abstract void test70() throws Exception;

    public abstract void test80() throws Exception;

    public abstract void test90() throws Exception;

    public abstract void test100() throws Exception;

    protected void testBrowserPage(XwadlFileConfig xwadlConfig) throws Exception {
        testBrowserPage(xwadlConfig, WARMUP_ROUNDS);
        ListListener listener = new ListListener();
        profilingService.setListener(listener);
        profilingService.setFilter("methodPanel.methodList.1.methodLink");
        testBrowserPage(xwadlConfig, TEST_ROUNDS);
        ProfilingUtil.writeToCsv(listener.getList(), xwadlConfig.toString(), this.getClass().getSimpleName());
    }

    private void testBrowserPage(XwadlFileConfig xwadlConfig, int rounds) throws Exception {
        for (int i = 0; i < rounds; i++) {
            String xwadlPath = XwadlFileBuilder.getInstance().createXwadlFile(xwadlConfig);

            tester.startPage(new BrowserPage(Model.of(new XmppURI("foo", xwadlPath))));
            tester.assertRenderedPage(BrowserPage.class);

            // test post method
            tester.clickLink("methodPanel:methodList:1:methodLink");
            tester.assertRenderedPage(BrowserPage.class);
        }
    }
}
