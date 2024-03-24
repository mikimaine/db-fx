package com.utd.library_db.database;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A custom thread factory for creating threads specifically for database connections.
 * This factory customizes thread naming and configuration for easier management and debugging.
 */
public class DatabaseThreadFactory implements ThreadFactory {
    // A static counter to keep track of the pool number for naming threads uniquely.
    // It's static so that the count is shared among all instances of this factory.
    static final AtomicInteger poolNumber = new AtomicInteger(1);

    // Creates a new Thread for the given Runnable task.
    // This method is called whenever a thread pool needs a new thread.
    @Override public Thread newThread(@NotNull Runnable runnable) {
        // Creates a new Thread with a custom name indicating it is a part of the database connection pool.
        // The name includes a unique pool number which increments with each thread creation.
        Thread thread = new Thread(runnable, "Database-Connection-" + poolNumber.getAndIncrement() + "-thread");

        // Set the thread as a daemon thread. Daemon threads do not prevent the JVM from exiting
        // when the program finishes and the thread is still running.
        // This is useful for long-running background tasks that should not block the application from shutting down.
        thread.setDaemon(true);

        return thread;
    }
}
