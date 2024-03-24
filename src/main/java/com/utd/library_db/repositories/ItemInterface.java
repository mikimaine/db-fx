package com.utd.library_db.repositories;

import java.sql.Date;

public interface ItemInterface {
    String bookTitle();
    String description();
    String isbn();
    int[] authorIds();
    Date publicationDate();
}
