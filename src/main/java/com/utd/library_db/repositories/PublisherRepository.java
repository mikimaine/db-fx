package com.utd.library_db.repositories;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import net.sf.persism.Session;

import static net.sf.persism.SQL.sql;

public class PublisherRepository {
    Connection conn;
    Session session;

    public PublisherRepository(Connection conn) {
        this.conn = conn;
        this.session = new Session(conn);
    }

    public List<Publisher> getPublishers() {
        return session.query(Publisher.class, sql("select * from publisher"));
    }

    public void addPublisher(PublisherCreate publisherCreate) throws SQLException, SQLException {

        String sql = "{CALL AddPublisher(?, ?)}";
        CallableStatement storedProc = conn.prepareCall(sql);
        storedProc.setString(1, publisherCreate.name());
        storedProc.setString(2, publisherCreate.phone_number());
        int affectedRows = storedProc.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Publisher saved successfully using stored procedure!");
        } else {
            System.out.println("Saving publisher using stored procedure failed.");
        }

    }
}
