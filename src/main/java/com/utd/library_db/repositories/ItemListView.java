package com.utd.library_db.repositories;

import net.sf.persism.annotations.NotTable;

@NotTable
public record ItemListView(int id, String title, String description, String isbn, String authors) {
}
