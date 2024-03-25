DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddPublisher`(IN publisherName VARCHAR(45), IN phoneNumber VARCHAR(12))
BEGIN
    INSERT INTO publisher (name, phone_number) VALUES (publisherName, phoneNumber);
END;;
DELIMITER ;