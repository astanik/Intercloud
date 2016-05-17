package de.tu_berlin.cit.intercloud.webapp;

import de.tu_berlin.cit.intercloud.client.profiling.impl.ProfilingService;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * This class listens to the begin and end of a request.
 * It notifies the {@link ProfilingService}.
 */
public class ProfilingRequestCycleListener extends AbstractRequestCycleListener {
    private ProfilingService profilingService = ProfilingService.getInstance();

    @Override
    public void onBeginRequest(RequestCycle cycle) {
        profilingService.start(cycle.getRequest().getUrl().toString());
    }

    @Override
    public void onEndRequest(RequestCycle cycle) {
        profilingService.stop();
    }
}
