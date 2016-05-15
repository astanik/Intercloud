package de.tu_berlin.cit.intercloud.client.profiling.api;

public interface IProfilingService {
    /**
     * Starts profiling of the current Thread
     * only if the {@code context} matches the specified {@code filter}.
     *
     * @param context Any context specifier e.g. a request.
     * @return A {@link ProfilingItem} that is used to profile the current Thread.
     * NULL if the context does not match the specified filter.
     */
    ProfilingItem start(String context);

    /**
     * Invokes a {@link IProfilingCommand} and
     * measures the execution time of its execute method.
     *
     * @param command The command to be executed.
     * @return The result of the command's execute method.
     */
    <T> T execute(IProfilingCommand<T> command);

    /**
     * Stop profiling of the current Thread.
     * @return The {@link ProfilingItem} of the current Thread.
     */
    ProfilingItem stop();

    /**
     * Specifies a filter whether or not to profile the current Thread or rather context.
     *
     * @param regex A regular expression matching the context to be profiled.
     */
    void setFilter(String regex);

    /**
     * Set a Listener which can process the measured execution times.
     * @param listener
     */
    void setListener(IProfilingListener listener);
}
