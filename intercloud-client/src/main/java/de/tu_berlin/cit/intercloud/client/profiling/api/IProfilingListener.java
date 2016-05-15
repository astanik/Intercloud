package de.tu_berlin.cit.intercloud.client.profiling.api;

/**
 * Implementations of this interface are able
 * to process a {@link ProfilingItem}.
 */
public interface IProfilingListener {

    /**
     * Listens to the start of a profiling context.
     * @param item
     */
    void onStart(ProfilingItem item);

    /**
     * Listens to the end of a profiling context.
     * @param item
     */
    void onStop(ProfilingItem item);
}
