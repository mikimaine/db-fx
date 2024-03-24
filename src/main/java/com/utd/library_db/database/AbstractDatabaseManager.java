package com.utd.library_db.database;

import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abstract class representing a generic database manager to handle common database operations.
 * The generic type <T> allows this class to be used for managing different types of entities.
 * -
 * Note: This class establishes the foundational structure and essential operations for managing database interactions,
 * leaving the specific implementation of CRUD operations (Create, Read, Update, Delete) to be defined in subclasses.
 **/
public abstract class AbstractDatabaseManager<T> implements DatabaseManager<T> {
    // Database connection URL
    private final String url;
    // Database username
    private final String username;
    // Database password
    private final String password;

    /**
     * Constructor to initialize the database manager with the necessary credentials and settings.
     * Constructs the JDBC URL for a MySQL database connection.
     * @param databaseName dbname
     * @param username dbUserName
     * @param password dbPassword
     */
    public AbstractDatabaseManager(String databaseName, String username, String password) {
        // Adjust for MySQL connection URL format
        // "jdbc:mysql://localhost/" is the JDBC connection URL prefix for MySQL.
        // `databaseName` is appended to specify which database to connect to.
        // "?serverTimezone=UTC" sets the server timezone configuration.
        this.url = "jdbc:mysql://localhost/" + databaseName + "?serverTimezone=UTC";
        this.username = username;
        this.password = password;
    }

    // Protected method to establish a connection with the database.
    // It uses the DriverManager to get a connection instance based on the provided credentials.
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}


