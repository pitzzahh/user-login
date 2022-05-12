package com.pitzzahh.exception;

/**
 * class used for exception handling if the user was not found.
 * This class extends the {@code RuntimeException}
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("USER DOES NOT EXIST");
    }
}