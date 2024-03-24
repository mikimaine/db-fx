DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertBookData`(
    IN `new_item_type_id` INT,
    IN `new_title` VARCHAR(45),
    IN `new_description` LONGTEXT,
    IN `new_location_in_library` VARCHAR(45),
    IN `new_genre` VARCHAR(45),
    IN `new_release_year` DATE,
    IN `new_language` VARCHAR(255),
    IN `new_publisher_id` INT,
    IN `new_edition_number` VARCHAR(45),
    IN `new_publication_date` DATE,
    IN `new_isbn` VARCHAR(17),
    IN `new_author_ids` TEXT -- Comma-separated list of author IDs
)
BEGIN
    DECLARE `new_item_id` INT;
    DECLARE `author_id` INT;
    DECLARE `idx` INT DEFAULT 1;
    DECLARE `ids_split` INT;

    -- Insert into `item`
    INSERT INTO `item` (`item_type_id`, `title`, `description`, `location_in_library`, `genre`, `release_year`, `language`, `publisher_id`)
    VALUES (`new_item_type_id`, `new_title`, `new_description`, `new_location_in_library`, `new_genre`, `new_release_year`, `new_language`, `new_publisher_id`);

    SET `new_item_id` = LAST_INSERT_ID();

    -- Insert into `book_edition`
    INSERT INTO `book_edition` (`item_id`, `edition_number`, `publication_date`, `isbn`)
    VALUES (`new_item_id`, `new_edition_number`, `new_publication_date`, `new_isbn`);

    -- Insert into `item_author`
    SET `ids_split` = CHAR_LENGTH(`new_author_ids`) - CHAR_LENGTH(REPLACE(`new_author_ids`, ',', '')) + 1;

    WHILE `idx` <= `ids_split` DO
            SET `author_id` = SUBSTRING_INDEX(SUBSTRING_INDEX(`new_author_ids`, ',', `idx`), ',', -1);
            INSERT INTO `item_author` (`item_id`, `author_id`)
            VALUES (`new_item_id`, `author_id`);
            SET `idx` = `idx` + 1;
        END WHILE;

END;;
DELIMITER ;