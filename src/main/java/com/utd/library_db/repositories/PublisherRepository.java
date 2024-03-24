package com.utd.library_db.repositories;

import java.sql.Connection;
import java.util.List;

import net.sf.persism.Session;

import static net.sf.persism.SQL.sql;

public class PublisherRepository {
    Connection conn;
    Session session;
    public PublisherRepository(Connection conn) {
        this.conn = conn;
        this.session =  new Session(conn);
    }
    public List<Publisher> getPublishers() {
        return session.query(Publisher.class, sql("select * from publisher"));
    }
}
