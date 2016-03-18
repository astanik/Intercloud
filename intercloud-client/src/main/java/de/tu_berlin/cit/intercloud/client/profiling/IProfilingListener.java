package de.tu_berlin.cit.intercloud.client.profiling;

public interface IProfilingListener {
    void onStart(ProfilingItem item);
    void onStop(ProfilingItem item);
}
