package de.tu_berlin.cit.intercloud.webapp.test;

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
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderOcciRequestRepresentationTest {
    private static final int WARMUP_ROUNDS = 10;
    private static final int TEST_ROUNDS = 40;
    private static final boolean HAS_DAFAULT = true;
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
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 10, 0, HAS_DAFAULT));
    }

    @Test
    public void twentyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 20, 0, HAS_DAFAULT));
    }

    @Test
    public void thrityCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 30, 0, HAS_DAFAULT));
    }

    @Test
    public void fourtyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 40, 0, HAS_DAFAULT));
    }

    @Test
    public void fiftyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 50, 0, HAS_DAFAULT));
    }

    @Test
    public void sixtyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 60, 0, HAS_DAFAULT));
    }

    @Test
    public void seventyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 70, 0, HAS_DAFAULT));
    }

    @Test
    public void eightyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 80, 0, HAS_DAFAULT));
    }

    @Test
    public void ninetyCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 90, 0, HAS_DAFAULT));
    }

    @Test
    public void hundredCategoryMixins() throws Exception {
        testBrowserPage(new XwadlFileConfig(false, 0, 0, 0, 100, 0, HAS_DAFAULT));
    }

    public void testBrowserPage(XwadlFileConfig xwadlConfig) throws Exception {
        testBrowserPage(xwadlConfig, WARMUP_ROUNDS);
        ListListener listener = new ListListener();
        //profilingService.setListener(new FileListener("target/" + xwadlConfig + ".csv"));
        profilingService.setListener(listener);
        profilingService.setFilter("methodTable.methodList.0.methodLink");
        testBrowserPage(xwadlConfig, TEST_ROUNDS);
        writeToCsv(aggregate(listener.getList()), xwadlConfig.toString());
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

    private void writeToCsv(Map<String, Double> map, String id) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new FileOutputStream("target/result.csv", true));
        try {
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
            entry.setValue(entry.getValue()/list.size());
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
