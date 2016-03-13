package de.tu_berlin.cit.intercloud.webapp.test.request;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import de.tu_berlin.cit.intercloud.client.profiling.ProfilingItem;
import de.tu_berlin.cit.intercloud.client.profiling.ProfilingService;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebApplication;
import de.tu_berlin.cit.intercloud.webapp.MockHelper;
import de.tu_berlin.cit.intercloud.webapp.XwadlFileBuilder;
import de.tu_berlin.cit.intercloud.webapp.XwadlFileConfig;
import de.tu_berlin.cit.intercloud.webapp.pages.BrowserPage;
import de.tu_berlin.cit.intercloud.webapp.profiling.ListListener;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BenchmarkOptions(warmupRounds = 0, benchmarkRounds = 10)
public abstract class AbstractRenderTest extends AbstractBenchmark {
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
        profilingService.setFilter("methodTable.methodList.0.methodLink");
        testBrowserPage(xwadlConfig, TEST_ROUNDS);
        writeToCsv(aggregate(listener.getList()), xwadlConfig.toString());
    }

    private void testBrowserPage(XwadlFileConfig xwadlConfig, int rounds) throws Exception {
        for (int i = 0; i < rounds; i++) {
            String xwadlPath = XwadlFileBuilder.getInstance().createXwadlFile(xwadlConfig);

            tester.startPage(new BrowserPage(Model.of(new XmppURI("foo", xwadlPath))));
            tester.assertRenderedPage(BrowserPage.class);

            // test post method
            tester.clickLink("methodTable:methodList:0:methodLink");
            tester.assertRenderedPage(BrowserPage.class);
        }
    }

    private void writeToCsv(Map<String, Double> map, String id) throws FileNotFoundException {
        File file = new File("target/" + this.getClass().getSimpleName() + ".csv");
        boolean fileExists = file.exists();
        PrintWriter writer = new PrintWriter(new FileOutputStream(file, true));
        try {
            if (!fileExists) {
                writer.println("id;request;onConfigure;onBeforeRender;onRender;transform");
            }
            StringBuilder s = new StringBuilder();
            s.append(id).append(";")
                    .append(map.get("request")).append(";")
                    .append(map.get("onConfigure")).append(";")
                    .append(map.get("onBeforeRender")).append(";")
                    .append(map.get("onRender")).append(";")
                    .append(map.get("transform")).append(";");
            writer.println(s);
        } finally {
            writer.close();
        }
    }

    private Map<String, Double> aggregate(List<ProfilingItem> list) {
        Map<String, Double> map = new HashMap<>();
        for (ProfilingItem item : list) {
            increment(map, "request", item.getDuration());
            increment(map, "onConfigure", item.getOnConfigure());
            increment(map, "onBeforeRender", item.getOnBeforeRender());
            increment(map, "onRender", item.getOnRender());
            increment(map, "transform", item.getTransform());
        }

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            entry.setValue(entry.getValue() / list.size());
        }
        return map;
    }

    private void increment(Map<String, Double> map, String name, long value) {
        Double d = map.get(name);
        if (null == d) {
            d = new Double(value);
        } else {
            d = d + value;
        }
        map.put(name, d);
    }
}
