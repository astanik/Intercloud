package de.tu_berlin.cit.intercloud.client.profiling;

public interface IProfilingListener {
    void start(ProfilingItem item);
    void stop(ProfilingItem item);
}
