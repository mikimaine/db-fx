DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateBookData`(
    IN `update_item_id` INT,
    IN `update_item_type_id` INT,
    IN `update_title` VARCHAR(45),
    IN `update_description` LONGTEXT,
    IN `update_location_in_library` VARCHAR(45),
    IN `update_genre` VARCHAR(45),
    IN `update_release_year` DATE,
    IN `update_language` VARCHAR(255),
    IN `update_publisher_id` INT,
    IN `update_edition_number` VARCHAR(45),
    IN `update_publication_date` DATE,
    IN `update_isbn` VARCHAR(17),
    IN `update_author_ids` TEXT -- Comma-separated list of author IDs
)
BEGIN
    DECLARE `author_id` INT;
    DECLARE `idx` INT DEFAULT 1;
    DECLARE `ids_split` INT;
    DECLARE `author_id_string` VARCHAR(255);
    DECLARE `current_position` INT DEFAULT 1;

    -- Update `item`
    UPDATE `item`
    SET `item_type_id` = `update_item_type_id`,
        `title` = `update_title`,
        `description` = `update_description`,
        `location_in_library` = `update_location_in_library`,
        `genre` = `update_genre`,
        `release_year` = `update_release_year`,
        `language` = `update_language`,
        `publisher_id` = `update_publisher_id`
    WHERE `id` = `update_item_id`;

    -- Update `book_edition`
    UPDATE `book_edition`
    SET `edition_number` = `update_edition_number`,
        `publication_date` = `update_publication_date`,
        `isbn` = `update_isbn`
    WHERE `item_id` = `update_item_id`;

    -- Update `item_author`
    DELETE FROM `item_author`
    WHERE `item_id` = `update_item_id`;

    SET `ids_split` = CHAR_LENGTH(`update_author_ids`) - CHAR_LENGTH(REPLACE(`update_author_ids`, ',', '')) + 1;

    WHILE `idx` <= `ids_split` DO
            SET `author_id_string` = SUBSTRING_INDEX(SUBSTRING_INDEX(`update_author_ids`, ',', `idx`), ',', -1);
            SET `author_id` = CAST(`author_id_string` AS UNSIGNED);
            INSERT INTO `item_author` (`item_id`, `author_id`)
            VALUES (`update_item_id`, `author_id`);
            SET `idx` = `idx` + 1;
        END WHILE;

END;;
DELIMITER ;
