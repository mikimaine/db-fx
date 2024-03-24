# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 11.3.2-MariaDB)
# Database: library
# Generation Time: 2024-03-24 22:25:17 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address_type` enum('local','home') NOT NULL,
  `address_line1` varchar(45) DEFAULT NULL,
  `address_line2` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `postal_code` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_At` datetime DEFAULT NULL,
  `profile_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `profile_id_indx_idx` (`profile_id`),
  CONSTRAINT `prodileid_address` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;

INSERT INTO `address` (`id`, `address_type`, `address_line1`, `address_line2`, `city`, `country`, `postal_code`, `created_at`, `updated_At`, `profile_id`)
VALUES
	(1,'local','123 Main St','Apt 4','Dallas','USA',75001,'2023-01-01 00:00:00','2024-01-01 00:00:00',1),
	(2,'home','456 Side St',NULL,'Plano','USA',75074,'2023-02-01 00:00:00','2024-02-01 00:00:00',2);

/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table author
# ------------------------------------------------------------

DROP TABLE IF EXISTS `author`;

CREATE TABLE `author` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `author_first_name_index` (`first_name`),
  KEY `author_first_name_last_name_index` (`first_name`,`last_name`),
  KEY `author_last_name_index` (`last_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;

INSERT INTO `author` (`id`, `first_name`, `middle_name`, `last_name`)
VALUES
	(1,'Shamkant','B.','Navathe'),
	(2,'Jane','Elizabeth','Smith'),
	(3,'William',NULL,'Brown'),
	(4,'sample',NULL,'autor'),
	(5,'Jams',NULL,'');

/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table book_edition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `book_edition`;

CREATE TABLE `book_edition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) DEFAULT NULL,
  `edition_number` varchar(45) NOT NULL,
  `publication_date` date NOT NULL,
  `isbn` varchar(17) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `book_edition_isbn_uindex` (`isbn`),
  KEY `edition_item_id_idx` (`item_id`),
  CONSTRAINT `edition_item_id` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `book_edition` WRITE;
/*!40000 ALTER TABLE `book_edition` DISABLE KEYS */;

INSERT INTO `book_edition` (`id`, `item_id`, `edition_number`, `publication_date`, `isbn`)
VALUES
	(1,1,'3rd Edition','2018-01-01','9780134685991'),
	(2,2,'3rd Edition','2018-01-01','9781492040347'),
	(3,3,'3rd Edition','2018-01-01','9781337627900'),
	(4,5,'1st Edition','2023-01-01','9780201422900');

/*!40000 ALTER TABLE `book_edition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table borrow
# ------------------------------------------------------------

DROP TABLE IF EXISTS `borrow`;

CREATE TABLE `borrow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `profile_id` int(11) NOT NULL,
  `checkout_date` date NOT NULL,
  `due_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `profile_id_idx` (`profile_id`),
  KEY `borrow_item_id_fk` (`item_id`),
  CONSTRAINT `borrow_item_id_fk` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
  CONSTRAINT `profile_id` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `borrow` WRITE;
/*!40000 ALTER TABLE `borrow` DISABLE KEYS */;

INSERT INTO `borrow` (`id`, `profile_id`, `checkout_date`, `due_date`, `return_date`, `item_id`)
VALUES
	(1,1,'2024-02-01','2024-03-01',NULL,1),
	(2,2,'2024-02-05','2024-03-05',NULL,2);

/*!40000 ALTER TABLE `borrow` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table borrowing_rule
# ------------------------------------------------------------

DROP TABLE IF EXISTS `borrowing_rule`;

CREATE TABLE `borrowing_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type` int(11) DEFAULT NULL,
  `max_allowed_item` int(11) NOT NULL,
  `effective_date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_idx` (`user_type`),
  CONSTRAINT `user_type_borrowing_rule` FOREIGN KEY (`user_type`) REFERENCES `user_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `borrowing_rule` WRITE;
/*!40000 ALTER TABLE `borrowing_rule` DISABLE KEYS */;

INSERT INTO `borrowing_rule` (`id`, `user_type`, `max_allowed_item`, `effective_date`)
VALUES
	(1,1,5,'2024-01-01'),
	(2,2,10,'2024-01-01'),
	(3,3,2,'2024-01-01');

/*!40000 ALTER TABLE `borrowing_rule` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cds_dvds_edition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cds_dvds_edition`;

CREATE TABLE `cds_dvds_edition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) DEFAULT NULL,
  `format` enum('CD','DVD') NOT NULL,
  `runtime_minutes` int(11) NOT NULL,
  `number_of_cd` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cds_dvds_edition_pk` (`item_id`),
  UNIQUE KEY `cds_dvds_edition_pk_3` (`item_id`),
  KEY `cds_dvds_edition_item_id_index` (`item_id`),
  CONSTRAINT `cds_dvds_edition_item_id_fk` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `cds_dvds_edition` WRITE;
/*!40000 ALTER TABLE `cds_dvds_edition` DISABLE KEYS */;

INSERT INTO `cds_dvds_edition` (`id`, `item_id`, `format`, `runtime_minutes`, `number_of_cd`)
VALUES
	(1,2,'DVD',148,1);

/*!40000 ALTER TABLE `cds_dvds_edition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table department
# ------------------------------------------------------------

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;

INSERT INTO `department` (`id`, `name`)
VALUES
	(1,'Human Resources'),
	(2,'Engineering'),
	(3,'Marketing');

/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table department_profile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `department_profile`;

CREATE TABLE `department_profile` (
  `department_id` int(11) DEFAULT NULL,
  `profile_id` int(11) DEFAULT NULL,
  KEY `profile_id_indx_idx` (`profile_id`),
  KEY `department_id_indx_idx` (`department_id`),
  CONSTRAINT `department_id_indx` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `profile_id_indx` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `department_profile` WRITE;
/*!40000 ALTER TABLE `department_profile` DISABLE KEYS */;

INSERT INTO `department_profile` (`department_id`, `profile_id`)
VALUES
	(1,1),
	(2,2);

/*!40000 ALTER TABLE `department_profile` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ebook_edition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ebook_edition`;

CREATE TABLE `ebook_edition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_Id` int(11) NOT NULL,
  `isbn` varchar(13) NOT NULL,
  `file_size_in_byte` int(11) NOT NULL,
  `file_format` enum('EPUB','PDF','MOBI') NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ebook_edition_pk_2` (`isbn`),
  UNIQUE KEY `ebook_edition_pk` (`item_Id`),
  KEY `ebook_edition_isbn_index` (`isbn`),
  CONSTRAINT `ebook_edition_item_id_fk` FOREIGN KEY (`item_Id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `ebook_edition` WRITE;
/*!40000 ALTER TABLE `ebook_edition` DISABLE KEYS */;

INSERT INTO `ebook_edition` (`id`, `item_Id`, `isbn`, `file_size_in_byte`, `file_format`, `url`)
VALUES
	(1,1,'9780321356680',5000000,'PDF','http://example.com/ebook/effectivejava.pdf');

/*!40000 ALTER TABLE `ebook_edition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table fine
# ------------------------------------------------------------

DROP TABLE IF EXISTS `fine`;

CREATE TABLE `fine` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `borrowing_id` int(11) NOT NULL,
  `fine_amount` float DEFAULT NULL,
  `fine_paid` tinyint(4) DEFAULT NULL,
  `fine_rule_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fine_rule_id_idx` (`fine_rule_id`),
  KEY `borrowing_id_idx` (`borrowing_id`),
  CONSTRAINT `borrowing_id` FOREIGN KEY (`borrowing_id`) REFERENCES `borrow` (`id`) ON UPDATE NO ACTION,
  CONSTRAINT `fine_rule_id` FOREIGN KEY (`fine_rule_id`) REFERENCES `fine_rule` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `fine` WRITE;
/*!40000 ALTER TABLE `fine` DISABLE KEYS */;

INSERT INTO `fine` (`id`, `borrowing_id`, `fine_amount`, `fine_paid`, `fine_rule_id`)
VALUES
	(1,1,2.5,0,1),
	(2,2,5,0,2);

/*!40000 ALTER TABLE `fine` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table fine_rule
# ------------------------------------------------------------

DROP TABLE IF EXISTS `fine_rule`;

CREATE TABLE `fine_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `unit` enum('day','week','month') NOT NULL,
  `maximum_fine_amount` varchar(45) NOT NULL,
  `item_type_id` int(11) NOT NULL,
  `effective_date` date NOT NULL,
  `effctive_date_end` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `item_type_id_idx` (`item_type_id`),
  CONSTRAINT `item_type_id` FOREIGN KEY (`item_type_id`) REFERENCES `item_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `fine_rule` WRITE;
/*!40000 ALTER TABLE `fine_rule` DISABLE KEYS */;

INSERT INTO `fine_rule` (`id`, `amount`, `unit`, `maximum_fine_amount`, `item_type_id`, `effective_date`, `effctive_date_end`)
VALUES
	(1,0.5,'day','5.00',1,'2024-01-01',NULL),
	(2,1,'day','10.00',2,'2024-01-01',NULL),
	(3,0.25,'day','2.50',3,'2024-01-01',NULL);

/*!40000 ALTER TABLE `fine_rule` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `item`;

CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_type_id` int(11) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `description` longtext DEFAULT NULL,
  `location_In_library` varchar(45) DEFAULT NULL,
  `genre` varchar(45) NOT NULL,
  `release_year` date NOT NULL,
  `language` varchar(255) NOT NULL,
  `publisher_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `item_item_type_idx` (`item_type_id`),
  KEY `item_genre_index` (`genre`),
  KEY `item_title_index` (`title`),
  KEY `item_release_year_index` (`release_year`),
  KEY `item_publisher_id_fk` (`publisher_id`),
  CONSTRAINT `item_item_type` FOREIGN KEY (`item_type_id`) REFERENCES `item_type` (`id`),
  CONSTRAINT `item_publisher_id_fk` FOREIGN KEY (`publisher_id`) REFERENCES `publisher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;

INSERT INTO `item` (`id`, `item_type_id`, `title`, `description`, `location_In_library`, `genre`, `release_year`, `language`, `publisher_id`)
VALUES
	(1,1,'Fundamentals Of Database System','A programming book about Java best practices','Shelf 1','Non-Fiction','2008-05-08','English',1),
	(2,2,'Inception','A science fiction heist thriller DVD','Shelf 2','Science Fiction','2010-07-16','English',2),
	(3,3,'Random Access Memories','A studio album by French electronic music duo Daft Punk','Shelf 3','Music','2013-05-17','English',3),
	(5,1,'Testing book','Lets test this thing','Location in library','Genre','2023-01-01','English',1);

/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table item_author
# ------------------------------------------------------------

DROP TABLE IF EXISTS `item_author`;

CREATE TABLE `item_author` (
  `item_id` int(11) DEFAULT NULL,
  `author_id` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `item_publisher_idx` (`item_id`),
  KEY `author_idx` (`author_id`),
  CONSTRAINT `item_author_author_id_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`),
  CONSTRAINT `item_author_item_id_fk` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `item_author` WRITE;
/*!40000 ALTER TABLE `item_author` DISABLE KEYS */;

INSERT INTO `item_author` (`item_id`, `author_id`, `id`)
VALUES
	(1,1,1),
	(1,2,2),
	(2,1,3),
	(3,2,4),
	(5,1,5);

/*!40000 ALTER TABLE `item_author` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table item_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `item_type`;

CREATE TABLE `item_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `item_type` WRITE;
/*!40000 ALTER TABLE `item_type` DISABLE KEYS */;

INSERT INTO `item_type` (`id`, `name`)
VALUES
	(1,'Book'),
	(2,'DVD'),
	(3,'CD');

/*!40000 ALTER TABLE `item_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table phone_number
# ------------------------------------------------------------

DROP TABLE IF EXISTS `phone_number`;

CREATE TABLE `phone_number` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `profile_id` int(11) NOT NULL,
  `phone_number` varchar(12) NOT NULL,
  `phone_type` enum('mobile','office','home') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone_number_UNIQUE` (`phone_number`),
  KEY `profile_id_idx` (`profile_id`),
  CONSTRAINT `profile_id_ind` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `phone_number` WRITE;
/*!40000 ALTER TABLE `phone_number` DISABLE KEYS */;

INSERT INTO `phone_number` (`id`, `profile_id`, `phone_number`, `phone_type`)
VALUES
	(1,1,'2141234567','mobile'),
	(2,2,'9727654321','office');

/*!40000 ALTER TABLE `phone_number` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table profile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `profile`;

CREATE TABLE `profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type_id` int(11) DEFAULT NULL,
  `first_name` varchar(45) NOT NULL,
  `middle_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) NOT NULL,
  `net_id` varchar(45) NOT NULL,
  `utd_id` varchar(45) NOT NULL,
  `email` varchar(255) NOT NULL,
  `gender` enum('M','F') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `net_id_UNIQUE` (`net_id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `utd_id_UNIQUE` (`utd_id`),
  KEY `id_idx` (`user_type_id`),
  KEY `profile_email_index` (`email`),
  KEY `profile_utd_id_index` (`utd_id`),
  CONSTRAINT `profile_user_type` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `profile` WRITE;
/*!40000 ALTER TABLE `profile` DISABLE KEYS */;

INSERT INTO `profile` (`id`, `user_type_id`, `first_name`, `middle_name`, `last_name`, `net_id`, `utd_id`, `email`, `gender`)
VALUES
	(1,1,'Alice','Jane','Doe','alice123','20240001','alice.doe@example.com','F'),
	(2,2,'Bob','Andrew','Smith','bob456','20240002','bob.smith@example.com','M'),
	(3,3,'Charlie',NULL,'Brown','charlie789','20240003','charlie.brown@example.com','M');

/*!40000 ALTER TABLE `profile` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table publisher
# ------------------------------------------------------------

DROP TABLE IF EXISTS `publisher`;

CREATE TABLE `publisher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `phone_number` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `publisher` WRITE;
/*!40000 ALTER TABLE `publisher` DISABLE KEYS */;

INSERT INTO `publisher` (`id`, `name`, `phone_number`)
VALUES
	(1,'Pearson','1234567890'),
	(2,'McGraw Hill','0987654321'),
	(3,'Penguin Random House','1122334455');

/*!40000 ALTER TABLE `publisher` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_type`;

CREATE TABLE `user_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`,`name`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `user_type` WRITE;
/*!40000 ALTER TABLE `user_type` DISABLE KEYS */;

INSERT INTO `user_type` (`id`, `name`)
VALUES
	(2,'Faculty'),
	(1,'Student'),
	(3,'Visitor');

/*!40000 ALTER TABLE `user_type` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping routines (PROCEDURE) for database 'library'
--
DELIMITER ;;

# Dump of PROCEDURE insertBookData
# ------------------------------------------------------------

/*!50003 DROP PROCEDURE IF EXISTS `insertBookData` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `insertBookData`(
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

END */;;

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE updateBookData
# ------------------------------------------------------------

/*!50003 DROP PROCEDURE IF EXISTS `updateBookData` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `updateBookData`(
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

END */;;

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
DELIMITER ;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
