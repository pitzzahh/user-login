package com.pitzzahh.exception;

/**
 * class used for exception handling on blank response.
 * This class extends the {@code RuntimeException}
 */
public class BlankResponseException extends RuntimeException {
    public BlankResponseException(String message) {
        super(message);
    }
}