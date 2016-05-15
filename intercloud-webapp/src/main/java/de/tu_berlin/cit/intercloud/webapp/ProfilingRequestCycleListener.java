package de.tu_berlin.cit.intercloud.webapp;

import de.tu_berlin.cit.intercloud.client.profiling.impl.ProfilingService;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

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
