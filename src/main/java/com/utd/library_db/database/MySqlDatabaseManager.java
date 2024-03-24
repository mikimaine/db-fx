package com.utd.library_db.database;

import com.utd.library_db.Config;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;

public class MySqlDatabaseManager<T> extends AbstractDatabaseManager<T> {
    private Class<T> type;
    private static final Logger logger = Logger.getLogger(MySqlDatabaseManager.class.getName());
    public MySqlDatabaseManager( Class<T> type) {
        super("library", Config.USERNAME, Config.PASSWORD);
        this.type = type;
    }
    public Connection getConnection() throws SQLException {
        logger.info("Getting a database connection");
        return super.getConnection();
    }


}
