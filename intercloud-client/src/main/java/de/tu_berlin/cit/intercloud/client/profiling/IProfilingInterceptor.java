package de.tu_berlin.cit.intercloud.client.profiling;

public interface IProfilingInterceptor<T> {
    void profile(ProfilingItem item, long millis);
    T invoke();
}
