package de.tu_berlin.cit.intercloud.webapp.profiling;

import de.tu_berlin.cit.intercloud.client.profiling.IProfilingListener;
import de.tu_berlin.cit.intercloud.client.profiling.ProfilingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ListListener implements IProfilingListener {
    private static final Logger logger = LoggerFactory.getLogger(ListListener.class);
    private List<ProfilingItem> list = new ArrayList<>();

    @Override
    public void onStart(ProfilingItem item) {
        list.add(item);
    }

    @Override
    public void onStop(ProfilingItem item) {
        logger.info("Profiling: {}", item);
    }

    public List<ProfilingItem> getList() {
        return list;
    }
}
