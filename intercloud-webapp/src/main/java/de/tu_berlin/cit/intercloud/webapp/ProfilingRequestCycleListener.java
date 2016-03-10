package de.tu_berlin.cit.intercloud.webapp;

import de.tu_berlin.cit.intercloud.client.profiling.ProfilingItem;
import de.tu_berlin.cit.intercloud.client.profiling.ProfilingService;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfilingRequestCycleListener extends AbstractRequestCycleListener {
    private static final Logger logger = LoggerFactory.getLogger(ProfilingRequestCycleListener.class);
    private ProfilingService profilingService = ProfilingService.getInstance();
    private long time;

    @Override
    public void onBeginRequest(RequestCycle cycle) {
        time = System.currentTimeMillis();
        profilingService.start(cycle.getRequest().getUrl().toString());
    }

    @Override
    public void onEndRequest(RequestCycle cycle) {
        ProfilingItem profilingItem = profilingService.getProfilingItem();
        if (null != profilingItem) {
            profilingItem.setRequestName(cycle.getRequest().getUrl().toString());
            profilingItem.setRequestDuration(System.currentTimeMillis() - time);
            profilingService.stop();
            logger.info("Processing Request: {}, {} ms", profilingItem.getRequestName(), profilingItem.getRequestDuration());
        }
    }
}
