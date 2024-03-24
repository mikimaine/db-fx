package com.utd.library_db.repositories;

import net.sf.persism.annotations.Table;

@Table("author")
public record Author(int id, String first_name, String middle_name, String last_name) {

    @Override
    public String toString() {
        return first_name + ' ' +last_name;
    }
}
