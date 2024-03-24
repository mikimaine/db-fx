package com.utd.library_db.repositories;

import net.sf.persism.annotations.Table;

@Table("publisher")
public record Publisher(int id, String name, String phone_number) {
    @Override
    public String toString() {
        return name;
    }
}
