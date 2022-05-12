package com.pitzzahh.exception;

/**
 * class used for exception handling on invalid username or password logins.
 * This class extends the {@code RuntimeException}
 */
public class InvalidUserNameOrPasswordException extends RuntimeException {
    public InvalidUserNameOrPasswordException(String message) {
        super(message);
    }
}