package com.utd.library_db.repositories;

import net.sf.persism.annotations.Table;

import java.sql.Date;

@Table("book_edition")
public record BookEdition(int id, int item_id, String edition_number, Date publication_date, String isbn) {
}
