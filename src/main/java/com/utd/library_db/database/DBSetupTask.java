package com.utd.library_db.database;

import java.sql.Connection;
import java.util.logging.Logger;

public class DBSetupTask extends DBTask<String> {
    private static final Logger logger = Logger.getLogger(DBTask.class.getName());

    @Override protected String call() throws Exception {
        try (Connection con = getConnection()) {
            logger.info("Connection successfully");
        }
        return "done";
    }
}