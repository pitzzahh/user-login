package com.pitzzahh.exception;

/**
 * class used for exception handling if the user already exists.
 * This class extends the {@code RuntimeException}
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String userName) {
        super(String.format("%s already exists", userName));
    }
}