package com.utd.library_db.repositories;

import net.sf.persism.annotations.Table;

import java.sql.Date;

@Table("item")
public record Item(int id, String title, String description, String location_in_library, String genre,
                   Date release_year, String language, int publisher_id) {
}
