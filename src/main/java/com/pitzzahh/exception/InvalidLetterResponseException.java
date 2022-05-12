package com.pitzzahh.exception;

/**
 * class used for exception handling on answers that are not a, b, or c.
 * This class extends the {@code RuntimeException}
 */
public class InvalidLetterResponseException extends RuntimeException{
    public InvalidLetterResponseException(String message) {
        super(message);
    }
}