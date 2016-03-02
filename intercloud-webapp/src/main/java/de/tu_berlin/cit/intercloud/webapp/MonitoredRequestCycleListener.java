package de.tu_berlin.cit.intercloud.webapp;

import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitoredRequestCycleListener extends AbstractRequestCycleListener {
    private static final Logger logger = LoggerFactory.getLogger(MonitoredRequestCycleListener.class);
    private long time;

    @Override
    public void onBeginRequest(RequestCycle cycle) {
        time = System.currentTimeMillis();
    }

    @Override
    public void onEndRequest(RequestCycle cycle) {
        logger.info("Processing Request: {}, {} ms", cycle.getRequest().getUrl(), System.currentTimeMillis() - time);
    }
}
