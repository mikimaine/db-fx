package com.utd.library_db.repositories;

import java.sql.Date;

public record ItemUpdate(Integer id, String bookTitle, String description, String isbn, int[] authorIds, String language, Integer publisherId, Date publicationDate) implements ItemInterface {
}
