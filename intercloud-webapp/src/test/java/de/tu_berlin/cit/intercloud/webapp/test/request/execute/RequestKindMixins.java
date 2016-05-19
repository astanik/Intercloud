package de.tu_berlin.cit.intercloud.webapp.test.request.execute;

import de.tu_berlin.cit.intercloud.webapp.XwadlFileConfig;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Ignore
public class RequestKindMixins extends AbstractRequestTest {
    private XwadlFileConfig xwadlConfig;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        xwadlConfig = new XwadlFileConfig();
        xwadlConfig.setHasKind(true);
        xwadlConfig.setHasDefaultValues(true);
    }

    @Test
    @Override
    public void test1() throws Exception {
        xwadlConfig.setNumOfKindMixins(1);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test10() throws Exception {
        xwadlConfig.setNumOfKindMixins(10);
        testBrowserPage(xwadlConfig);

    }

    @Test
    @Override
    public void test20() throws Exception {
        xwadlConfig.setNumOfKindMixins(20);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test30() throws Exception {
        xwadlConfig.setNumOfKindMixins(30);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test40() throws Exception {
        xwadlConfig.setNumOfKindMixins(40);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test50() throws Exception {
        xwadlConfig.setNumOfKindMixins(50);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test60() throws Exception {
        xwadlConfig.setNumOfKindMixins(60);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test70() throws Exception {
        xwadlConfig.setNumOfKindMixins(70);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test80() throws Exception {
        xwadlConfig.setNumOfKindMixins(80);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test90() throws Exception {
        xwadlConfig.setNumOfKindMixins(90);
        testBrowserPage(xwadlConfig);
    }

    @Test
    @Override
    public void test100() throws Exception {
        xwadlConfig.setNumOfKindMixins(100);
        testBrowserPage(xwadlConfig);
    }
}
