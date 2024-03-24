package com.utd.library_db.database;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class DBTask, extending Task, to handle database-related tasks asynchronously.
 * It provides a template for executing database operations in the background.
 * The generic type <T> allows this class to be used for various types of tasks that return different data types.
 * @param <T>
 */
public abstract class DBTask<T> extends Task<T> {
    // Logger for logging errors. It's static and final since it's common to all instances of DBTask
    // and its subclasses, and its value does not change once assigned.
    private static final Logger logger = Logger.getLogger(DBTask.class.getName());

    // Constructor for DBTask which sets up the failure event handling.
    // When the task fails, it logs the exception at a SEVERE level.
    public DBTask() {
        // Set up a failure event handler that logs the exception when the task fails.
        setOnFailed(t -> logger.log(Level.SEVERE, null, getException()));
    }

    // Protected method to establish a connection with the database.
    // It creates an instance of MySqlDatabaseManager specifically for DBSetupTask class
    // and then uses it to obtain a database connection.
    protected Connection getConnection() throws SQLException {
        // Instance of MySqlDatabaseManager for handling database connections.
        // The generic type DBSetupTask is specified here, indicating the specific task or entity type
        // this DBTask is meant to manage in terms of database operations.
        MySqlDatabaseManager<DBSetupTask> dbManager = new MySqlDatabaseManager<>(DBSetupTask.class);
        return dbManager.getConnection();
    }
}