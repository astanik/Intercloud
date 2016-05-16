package de.tu_berlin.cit.intercloud.client.profiling.api;

/**
 * The duration to execute an implementation of this command can
 * by measured by an {@link IProfilingService}.
 *
 * @param <T> The Class of the execute method's result.
 */
public interface IProfilingCommand<T> {

    /**
     * @return An identifier to associate the Command with.
     * It is used to store the measured duration into a {@link ProfilingItem}.
     */
    String getIdentifier();

    /**
     * Defines a Command to be executed.
     *
     * @return The Command's result.
     */
    T execute();
}
