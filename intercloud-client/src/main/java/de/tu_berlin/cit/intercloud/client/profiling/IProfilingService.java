package de.tu_berlin.cit.intercloud.client.profiling;

public interface IProfilingService {
    /**
     * Starts profiling of the current Thread
     * only if the context matches the specified filter.
     *
     * @param context Any context specifier e.g. a request.
     * @return A {@link ProfilingItem} that is used to profile the current Thread.
     * NULL if the context does not match the specified filter.
     */
    ProfilingItem start(String context);

    /**
     * Invokes a {@link IProfilingTask} and
     * measures the execution time of its invoke method.
     *
     * @param interceptor
     * @return The result of the interceptor's invoke method.
     */
    <T> T invokeAndProfile(IProfilingTask<T> interceptor);

    /**
     * Stop profiling of the current Thread.
     */
    ProfilingItem stop();

    /**
     * Specifies a filter whether or not to profile the current Thread.
     *
     * @param regex A regular expression matching the context to be profiled.
     */
    void setFilter(String regex);

    void setListener(IProfilingListener listener);
}
