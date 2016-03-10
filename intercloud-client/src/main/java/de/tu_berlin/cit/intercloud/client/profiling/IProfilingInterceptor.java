package de.tu_berlin.cit.intercloud.client.profiling;

public interface IProfilingInterceptor {
    void profile(ProfilingItem item, long millis);
    Object invoke();
}
