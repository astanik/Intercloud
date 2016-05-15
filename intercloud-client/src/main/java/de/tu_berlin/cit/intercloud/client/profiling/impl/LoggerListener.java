package de.tu_berlin.cit.intercloud.client.profiling.impl;

import de.tu_berlin.cit.intercloud.client.profiling.api.IProfilingListener;
import de.tu_berlin.cit.intercloud.client.profiling.api.ProfilingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerListener implements IProfilingListener {
    private final static Logger logger = LoggerFactory.getLogger(LoggerListener.class);
    @Override
    public void onStart(ProfilingItem item) {

    }

    @Override
    public void onStop(ProfilingItem item) {
        logger.info("profile: {}", item);
    }
}
