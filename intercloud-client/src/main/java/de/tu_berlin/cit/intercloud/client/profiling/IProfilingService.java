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
     * Get the {@link ProfilingItem} of the current Thread.
     *
     * @return An instance if start was successful, NULL if not.
     */
    ProfilingItem getProfilingItem();

    /**
     * Invokes a {@link IProfilingInterceptor} and
     * measures the execution time of its invoke method.
     * @param interceptor
     * @return The result of the interceptor's invoke method.
     */
    Object invokeAndProfile(IProfilingInterceptor interceptor);

    /**
     * Stop profiling of the current Thread.
     */
    void stop();

    /**
     * Specifies a filter whether or not to profile the current Thread.
     * @param regex
     */
    void setFilter(String regex);
}
