package com.pitzzahh.exception;

/**
 * class used for exception handling on number response.
 * This class extends the {@code RuntimeException}
 */
public class NumberResponseException extends RuntimeException {
    public NumberResponseException(String message) {
        super(message);
    }
}