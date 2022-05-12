package com.pitzzahh.entity;

/**
 * A record makes the code clean. getters, setters and other methods
 * are automatically generated as well us hashcode, toString and equals method.
 * I made my own implementation of toString() method.
 */
public record User(String username, String password) {
    @Override
    public String toString() {
        return "Username = " + username + "\n" +
               "Password = " + password + "\n";
    }
}