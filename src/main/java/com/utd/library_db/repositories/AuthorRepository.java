package com.utd.library_db.repositories;

import net.sf.persism.Session;

import java.sql.Connection;
import java.util.List;

import static net.sf.persism.SQL.sql;

public class AuthorRepository {
    Connection conn;
    Session session;
    public AuthorRepository(Connection conn) {
        this.conn = conn;
        this.session =  new Session(conn);
    }
    public List<Author> getAuthors() {
        return session.query(Author.class, sql("select * from author"));
    }
}
