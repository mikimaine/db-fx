package com.utd.library_db.repositories;

import net.sf.persism.Session;

import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Connection;
import java.util.stream.Collectors;

import static net.sf.persism.Parameters.params;
import static net.sf.persism.SQL.*;

public class ItemRepository {
    Session session;
    Connection conn;
    public ItemRepository(Connection conn) {
        this.session = new Session(conn);
        this.conn = conn;
    }

    /**
     * Filters items in the database based on various criteria.
     *
     * @param publisherId The publisher's ID to filter by; if null, the filter is not applied.
     * @param isbn The ISBN to filter by; if null or blank, the filter is not applied.
     * @param title The title to filter by; if null or blank, the filter is not applied.
     * @param authors An array of author names to filter by; if null or empty, the filter is not applied.
     * @return A list of ItemListView objects that match the filter criteria.
     * .
     * This method builds a dynamic SQL query to filter items in the database based on the specified
     * parameters. It constructs the SQL query by appending conditions for each filter if they are not null
     * or empty. For the authors filter, it searches for items that have any of the specified authors.
     * The SQL query uses JOINs to connect items with their authors and editions, and aggregates
     * author names using GROUP_CONCAT to create a comma-separated list of authors per item.
     * .
     * The method then executes the query and returns the list of items that match the criteria,
     * represented as ItemListView objects. This allows for flexible searching in the database
     * by combining various filter options.
     */
    public List<ItemListView> filterItems(Integer publisherId, String isbn, String title, String[] authors) {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("""
                SELECT i.id, i.title, i.description, e.isbn,
                       GROUP_CONCAT(DISTINCT CONCAT(a.first_name, ' ', a.last_name)
                       ORDER BY a.first_name ASC, a.last_name ASC SEPARATOR ', ') AS authors
                FROM item as i
                LEFT JOIN book_edition as e on i.id = e.item_id
                LEFT JOIN item_author as ia on i.id = ia.item_id
                LEFT JOIN author as a on ia.author_id = a.id
                WHERE 1 = 1
                """);

        if (publisherId != null) {
            sqlBuilder.append(" AND i.publisher_id = ?");
            parameters.add(publisherId);
        }
        if (isbn != null && !isbn.isBlank()) {
            sqlBuilder.append(" AND e.isbn = ?");
            parameters.add(isbn);
        }
        if (title != null && !title.isBlank()) {
            sqlBuilder.append(" AND i.title LIKE ?");
            parameters.add("%" + title + "%");
        }
        if (authors != null && authors.length > 0) {
            sqlBuilder.append(" AND (");
            for (int i = 0; i < authors.length; i++) {
                sqlBuilder.append("CONCAT(a.first_name, ' ', a.last_name) LIKE ?");
                parameters.add("%" + authors[i] + "%");
                if (i < authors.length - 1) {
                    sqlBuilder.append(" OR ");
                }
            }
            sqlBuilder.append(")");
        }

        sqlBuilder.append(" GROUP BY i.id, e.id");

        return session.query(
                ItemListView.class,
                sql(sqlBuilder.toString()),
                params(parameters.toArray())
        );
    }

    /**
     * Adds a new item to the database using a stored procedure.
     * This method prepares and executes a stored procedure call with the new item details.
     *
     * @param newItem The new item data to be inserted into the database.
     * @throws Exception If there is a database access error or other errors.
     * .
     * The stored procedure 'insertBookData' is expected to insert a new book record
     * into the database. The method sets the parameters for this stored procedure
     * with the attributes of the `newItem` object.
     * .
     * Parameters for the stored procedure include book title, description, location in library,
     * genre, publication date, language, publisher ID, edition number, publication date, ISBN,
     * and author IDs. These are set from the `newItem` object's properties.
     * .
     * The author IDs are provided as an array and are converted to a comma-separated string to
     * fit the stored procedure's expected parameter type.
     * .
     * After setting all necessary parameters, the stored procedure is executed, and a
     * confirmation message is printed to the console indicating the operation's success.
     */
    public void addItem(ItemCreate newItem) throws Exception {
        // Prepare the stored procedure call
        String sql = "{CALL insertBookData(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement stmt = conn.prepareCall(sql);

        // Set the parameters for the stored procedure
        stmt.setInt(1, 1); // new_item_type_id
        stmt.setString(2, newItem.bookTitle()); // new_title
        stmt.setString(3, newItem.description()); // new_description
        stmt.setString(4, "Location in library"); // new_location_in_library
        stmt.setString(5, "Genre"); // new_genre
        stmt.setDate(6, newItem.publicationDate()); // new_release_year
        stmt.setString(7, "English"); // new_language
        stmt.setInt(8, 1); // new_publisher_id
        stmt.setString(9, "1st Edition"); // new_edition_number
        stmt.setDate(10, newItem.publicationDate()); // new_publication_date
        stmt.setString(11, newItem.isbn()); // new_isbn

        String authorIdsString = Arrays.stream(newItem.authorIds())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        stmt.setString(12, authorIdsString); // new_author_id
        // Execute the stored procedure
        stmt.execute();

        System.out.println("Stored procedure executed successfully.");
    }

    /**
     * Updates the details of an existing item in the database using a stored procedure.
     * This method prepares and executes a stored procedure call with the updated item details.
     *
     * @param updatedItem The updated item data to be saved to the database.
     * @throws Exception If there is a database access error or other errors.
     * .
     * The stored procedure is called 'updateBookData' and expects various parameters
     * related to the book's attributes like title, description, publication date, etc.
     * These parameters are set from the `updatedItem` object's properties.
     * .
     * The method sets each parameter of the stored procedure call with the corresponding
     * attribute of `updatedItem`. For example, the item ID and title are set from the
     * updatedItem's id and bookTitle properties, respectively. It also converts the array
     * of author IDs to a comma-separated string to match the expected stored procedure parameter type.
     * .
     * After setting all the necessary parameters, it executes the stored procedure and
     * prints a confirmation message to the console.
     */
    public void updateItem(ItemUpdate updatedItem) throws Exception {
        // Prepare the stored procedure call
        String sql = "{CALL updateBookData(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement stmt = conn.prepareCall(sql);

        // Set the parameters for the stored procedure
        stmt.setInt(1, updatedItem.id()); // update_item_id
        stmt.setInt(2, 1); // update_item_type_id
        stmt.setString(3, updatedItem.bookTitle()); // update_title
        stmt.setString(4, updatedItem.description()); // update_description
        stmt.setString(5, "Location in library"); // update_location_in_library
        stmt.setString(6, "Genre"); // update_genre
        stmt.setDate(7, updatedItem.publicationDate()); // update_release_year
        stmt.setString(8, updatedItem.language()); // update_language
        stmt.setInt(9, updatedItem.publisherId()); // update_publisher_id
        stmt.setString(10, "1"); // update_edition_number
        stmt.setDate(11, updatedItem.publicationDate()); // update_publication_date
        stmt.setString(12, updatedItem.isbn()); // update_isbn

        String authorIdsString = Arrays.stream(updatedItem.authorIds())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
        stmt.setString(13, authorIdsString); // update_author_ids

        // Execute the stored procedure
        stmt.execute();

        System.out.println("Stored procedure executed successfully.");
    }


    /**
     * Removes an item and its related data from the database.
     *
     * @param itemId The ID of the item to be removed.
     * .
     * This method performs a database transaction to remove an item from the database
     * along with its associated data in a series of steps:
     * 1. Deletes all entries in the ItemAuthor table related to the item, removing the
     *    association between the item and its authors.
     * 2. Deletes all entries in the BookEdition table related to the item, removing
     *    any book edition details associated with the item.
     * 3. Deletes the item from the Item table itself.
     * .
     * These deletions ensure that all references to the item in the database are removed,
     * maintaining referential integrity. The operations are executed within a database
     * transaction to ensure that either all deletions succeed or none do, preserving
     * the consistency of the database state.
     */
    public void removeItem(int itemId) {
        // Start a database transaction using the session object.
        session.withTransaction(() -> {
            // Delete entries from the ItemAuthor table where the item_id matches the provided id.
            // This removes all author associations with the item to be deleted.
            session.delete(ItemAuthor.class, where("item_id=?"), params(itemId));

            // Delete entries from the BookEdition table where the item_id matches the provided id.
            // This removes all book editions associated with the item to be deleted.
            session.delete(BookEdition.class, where("item_id=?"), params(itemId));

            // Finally, delete the entry from the Item table where the id matches the provided id.
            // This removes the item itself after removing its associations.
            session.delete(Item.class, where("id=?"), params(itemId));
        });
    }
}
