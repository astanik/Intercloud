package de.tu_berlin.cit.intercloud.client.profiling.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to collect the execution times of
 * executed {@link IProfilingCommand}s during the lifetime of
 * a thread or rather HTTP request.
 */
public class ProfilingItem {

    // The profiling context e.g. the name/uri of the current request.
    private final String context;
    // The duration of the executed HTTP request.
    private long duration;
    // Contains the execution times of IProfilingCommands executed during the request.
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

    /**
     * Sets the execution time of an {@link IProfilingCommand}.
     * @param identifier The Profiling Command's identifier provided by its getIdentifier().
     * @param duration The execution time of an Profiling Command.
     */
    public void add(String identifier, long duration) {
        durationMap.put(identifier, duration);
    }

    /**
     * @param identifier The Profiling Command's identifier provided by its getIdentifier().
     * @return The duration of an {@link IProfilingCommand}'s execution time.
     */
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
