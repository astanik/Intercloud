package de.tu_berlin.cit.intercloud.webapp.test.request;

import de.tu_berlin.cit.intercloud.webapp.XwadlFileConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RenderLinkMixins extends AbstractRenderTest {
    private XwadlFileConfig xwadlConfig;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        xwadlConfig = new XwadlFileConfig();
        xwadlConfig.setNumOfLinks(5);
    }

    @Test
    @Override
    public void test1() throws Exception {
        xwadlConfig.setNumOfLinkMixins(1);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test10() throws Exception {
        xwadlConfig.setNumOfLinkMixins(10);
        testBrowserPage(xwadlConfig);

    }

    @Test
    @Override
    public void test20() throws Exception {
        xwadlConfig.setNumOfLinkMixins(20);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test30() throws Exception {
        xwadlConfig.setNumOfLinkMixins(30);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test40() throws Exception {
        xwadlConfig.setNumOfLinkMixins(40);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test50() throws Exception {
        xwadlConfig.setNumOfLinkMixins(50);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test60() throws Exception {
        xwadlConfig.setNumOfLinkMixins(60);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test70() throws Exception {
        xwadlConfig.setNumOfLinkMixins(70);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test80() throws Exception {
        xwadlConfig.setNumOfLinkMixins(80);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test90() throws Exception {
        xwadlConfig.setNumOfLinkMixins(90);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test100() throws Exception {
        xwadlConfig.setNumOfLinkMixins(100);
        testBrowserPage(xwadlConfig);
    }
}
