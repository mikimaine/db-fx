package com.utd.library_db.repositories;

import java.sql.Date;

public record ItemCreate(String bookTitle, String description, String isbn, int[] authorIds, Date publicationDate) implements ItemInterface {
}
