package com.utd.library_db.repositories;

import net.sf.persism.annotations.NotTable;
@NotTable
public record PublisherCreate(String name, String phone_number) {
}
