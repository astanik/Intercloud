package de.tu_berlin.cit.intercloud.webapp.test.response;

import de.tu_berlin.cit.intercloud.webapp.XwadlFileConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ResponseLinks extends AbstractResponseRenderTest {
    private XwadlFileConfig xwadlConfig;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        xwadlConfig = new XwadlFileConfig();
        xwadlConfig.setNumOfLinkMixins(5);
    }

    @Test
    @Override
    public void test1() throws Exception {
        xwadlConfig.setNumOfLinks(1);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test10() throws Exception {
        xwadlConfig.setNumOfLinks(10);
        testBrowserPage(xwadlConfig);

    }

    @Test
    @Override
    public void test20() throws Exception {
        xwadlConfig.setNumOfLinks(20);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test30() throws Exception {
        xwadlConfig.setNumOfLinks(30);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test40() throws Exception {
        xwadlConfig.setNumOfLinks(40);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test50() throws Exception {
        xwadlConfig.setNumOfLinks(50);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test60() throws Exception {
        xwadlConfig.setNumOfLinks(60);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test70() throws Exception {
        xwadlConfig.setNumOfLinks(70);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test80() throws Exception {
        xwadlConfig.setNumOfLinks(80);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test90() throws Exception {
        xwadlConfig.setNumOfLinks(90);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test100() throws Exception {
        xwadlConfig.setNumOfLinks(100);
        testBrowserPage(xwadlConfig);
    }
}
