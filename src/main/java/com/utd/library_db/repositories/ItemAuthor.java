package com.utd.library_db.repositories;

import net.sf.persism.annotations.Table;

@Table("item_author")
public record ItemAuthor(int item_id, int author_id, int id) {
}
