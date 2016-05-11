package de.tu_berlin.cit.intercloud.client.profiling;

import java.util.HashMap;
import java.util.Map;

public class ProfilingItem {

    private final String context;
    private long duration;
    private final Map<String, Long> durationMap = new HashMap<>();

    public ProfilingItem(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void add(String identifier, long duration) {
        durationMap.put(identifier, duration);
    }

    public long get(String identifier) {
        Long l = durationMap.get(identifier);
        return null != l ? l : 0;
    }

    @Override
    public String toString() {
        return "ProfilingItem{" +
                "context='" + context + '\'' +
                ", duration=" + duration +
                ", durationMap=" + durationMap +
                '}';
    }
}
