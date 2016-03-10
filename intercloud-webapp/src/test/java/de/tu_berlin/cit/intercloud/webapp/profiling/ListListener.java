package de.tu_berlin.cit.intercloud.webapp.profiling;

import de.tu_berlin.cit.intercloud.client.profiling.IProfilingListener;
import de.tu_berlin.cit.intercloud.client.profiling.ProfilingItem;

import java.util.ArrayList;
import java.util.List;

public class ListListener implements IProfilingListener {
    private List<ProfilingItem> items = new ArrayList<>();

    @Override
    public void start(ProfilingItem item) {
        items.add(item);
    }

    @Override
    public void stop(ProfilingItem item) {

    }
}
