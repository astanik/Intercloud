package de.tu_berlin.cit.intercloud.client.profiling;

public interface IProfilingTask<T> {
    String getIdentifier();
    T invoke();
}
