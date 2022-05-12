package com.pitzzahh.exception;

/**
 * class used for exception handling on special character response.
 * Except alphanumeric characters.
 * This class extends the {@code RuntimeException}
 */
public class SpecialCharacterResponseException extends RuntimeException {
    public SpecialCharacterResponseException(String message) {
        super(message);
    }
}