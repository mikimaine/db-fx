package com.utd.library_db;

import com.utd.library_db.repositories.Author;

import java.util.List;

public class Util {
    public static final String[] COMMON_LANGUAGES = {
            "English", "Mandarin Chinese", "Hindi", "Spanish",
            "French", "Arabic", "Bengali", "Russian",
            "Portuguese", "Indonesian"
    };

    public static boolean validateISBN(String isbn) {
        isbn = isbn.replaceAll("-", ""); // Remove hyphens if present

        if (isbn.matches("^\\d{10}$|^\\d{13}$")) { // Check if it has 10 or 13 digits only
            if (isbn.length() == 10) {
                return isValidISBN10(isbn);
            } else {
                return isValidISBN13(isbn);
            }
        } else {
            // Invalid format
            // Show some error message to user
            return false;
        }
    }
    /**
     * Validates the format of a 10-digit ISBN (International Standard Book Number).
     *
     * @param isbn The ISBN string to be validated.
     * @return true if the ISBN is valid, false otherwise.
     * .
     * The method implements the ISBN-10 validation process, which includes the following steps:
     * 1. The first 9 characters must be digits (0-9). Each digit is multiplied by a decreasing
     *    weight, starting from 10 down to 2, and these products are summed.
     * 2. The last character can be a digit or 'X', which represents the value 10.
     * 3. The sum of the weighted digits plus the value of the last character must be divisible by 11
     *    for the ISBN to be considered valid.
     * .
     * If any character in the first 9 digits is not a number, or if the last character is not a number
     * nor 'X', the ISBN is immediately considered invalid. If all characters are valid, the final
     * computed sum is checked for divisibility by 11 to determine the ISBN's validity.
     */
    private static boolean isValidISBN10(String isbn) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = isbn.charAt(i) - '0'; // Convert char to integer value
            if (0 > digit || 9 < digit) return false; // Check if each of the first 9 chars is a digit
            sum += (digit * (10 - i)); // Multiply the digit by its weight and add to sum
        }
        char lastChar = isbn.charAt(9); // The 10th character can be 0-9 or 'X'
        if (lastChar != 'X' && (lastChar < '0' || lastChar > '9')) return false; // Validate the last character
        sum += (lastChar == 'X') ? 10 : (lastChar - '0'); // Add the value of the last character to sum
        return (sum % 11 == 0); // Check if sum is divisible by 11 for a valid ISBN
    }

    /**
     * Validates the format of a 13-digit ISBN (International Standard Book Number).
     *
     * @param isbn The ISBN string to be validated.
     * @return true if the ISBN is valid, false otherwise.
     * .
     * This method checks the validity of an ISBN-13 string based on the following criteria:
     * 1. All characters must be digits (0-9).
     * 2. The first 12 digits are used to calculate a sum, where each digit is multiplied by either
     *    1 or 3. The multiplier alternates between 1 and 3 for each digit (starting with 1 for the
     *    first digit).
     * 3. The 13th digit (last digit) is added to this sum.
     * 4. To be valid, the final sum must be divisible by 10.
     * .
     * The method iterates through the first 12 characters of the ISBN, converting each to an integer
     * and accumulating a weighted sum. If any character in these first 12 positions is not a digit,
     * the ISBN is immediately considered invalid. The last character is then added to the sum,
     * and the total is checked for divisibility by 10 to confirm the validity of the ISBN.
     */
    private static boolean isValidISBN13(String isbn) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = isbn.charAt(i) - '0'; // Convert char to integer value
            if (0 > digit || 9 < digit) return false; // Check if each char is a digit
            sum += (digit * ((i % 2 == 0) ? 1 : 3)); // Alternate multiplier between 1 and 3, starting with 1
        }
        int lastDigit = isbn.charAt(12) - '0';  // Convert the last character to an integer
        sum += lastDigit;  // Add the last digit to the sum
        return (sum % 10 == 0);  // Check if the total sum is divisible by 10 for a valid ISBN-13
    }

    /**
     * Retrieves the IDs of authors from a list of full names, given a list of Author objects.
     *
     * @param authorsFirstAndLastName An array of strings containing the first and last names of authors.
     * @param authorsList A list of Author objects to search through for matching names.
     * @return An array of integers representing the IDs of the authors, corresponding to the input names.
     *         If an author's name does not match any in the authorsList, their ID in the array will be -1.
     * .
     * This method processes an array of full names (assumed to be "first last" format) and a list of Author
     * objects to find the ID of each author in the input array. It splits each full name into first and
     * last names, then searches through the provided list of Author objects to find a matching author.
     * When a match is found, it assigns the corresponding author's ID to the output array. If no match is
     * found, the author ID is set to -1. The method returns an array of these IDs, maintaining the order
     * of the input names.
     */
    public static int[] getAuthorIdsFromNameList(String[] authorsFirstAndLastName, List<Author> authorsList) {
        int[] authorIds = new int[authorsFirstAndLastName.length];
        for (int i = 0; i < authorsFirstAndLastName.length; i++) {
            String fullName = authorsFirstAndLastName[i];
            String[] names = fullName.split(" "); // Split the full name into first and last names
            authorIds[i] = -1; // Default value if author ID is not found
            for (Author author : authorsList) {
                // Check if both first and last names match with any author in the list
                if (names.length == 2 && author.first_name().equals(names[0]) && author.last_name().equals(names[1])) {
                    authorIds[i] = author.id(); // Set the author ID in the array
                    break; // Exit the loop once the matching author is found
                }
            }
        }
        return authorIds; // Return the array of author IDs
    }

    public static String validateBookData(String isbn, String title, int[] authorIds, int publisherId, String language, String description) {
        StringBuilder errors = new StringBuilder();

        if (isbn == null || isbn.isEmpty()) {
            errors.append("ISBN cannot be empty.\n\n");
        } else if (!Util.validateISBN(isbn)) {
            errors.append("ISBN is invalid.\n\n");
        }
        if (title == null || title.isEmpty()) {
            errors.append("Title cannot be empty.\n\n");
        }
        if (authorIds.length == 0) {
            errors.append("At least one author must be selected.\n\n");
        }
        if (publisherId == 0) {
            errors.append("Publisher must be selected.\n\n");
        }
        if (language == null || language.isEmpty()) {
            errors.append("Language must be specified.\n\n");
        }
        if (description==null || description.isEmpty()) {
            errors.append("Description cannot be empty.\n\n");
        }

        return errors.toString();
    }
}
