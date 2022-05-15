package com.pitzzahh.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.pitzzahh.database.DatabaseConnection;

class ValidationTest {

    @Test
    void shouldPassIfUserNameIsValid() {
        // given
        String username = "Password@123";
        // when
        boolean result = Validation.isUserNameOrPasswordValid.test(username);
        // then
        Assertions.assertTrue(result);
    }
    @Test
    void shouldPassIfUserNameExist() {
        // given
        String userNameToCheck = "pitzzahh";
        DatabaseConnection.CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS credentials (" +
                                                    "user_name VARCHAR(20) NOT NULL PRIMARY KEY," +
                                                    "password VARCHAR(20) NOT NULL);";
        DatabaseConnection databaseConnection = new DatabaseConnection();
        // when
        boolean result = Validation.doesUserNameExist.test(userNameToCheck, databaseConnection);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    void shouldPassIfUserNameDoesNotExist() {
        // given
        String userNameToCheck = "beluga";
        DatabaseConnection.CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS credentials (" +
                                                    "user_name VARCHAR(20) NOT NULL PRIMARY KEY," +
                                                    "password VARCHAR(20) NOT NULL);";
        DatabaseConnection databaseConnection = new DatabaseConnection();
        // when
        boolean result = Validation.doesUserNameExist.test(userNameToCheck, databaseConnection);
        // then
        Assertions.assertFalse(result);
    }

    @Test
    void shouldPassIfPasswordIsFound() {
        // given
        String username = "pitzzahh";
        String expectedPassword = "!Password123";
        DatabaseConnection.CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS credentials (" +
                                                    "user_name VARCHAR(20) NOT NULL PRIMARY KEY," +
                                                    "password VARCHAR(20) NOT NULL);";
        DatabaseConnection databaseConnection = new DatabaseConnection();
        // when
        String result = Validation.getPassword(databaseConnection, username);
        // then
        Assertions.assertEquals(expectedPassword, result);
    }

    @Test
    void shouldPassIfTheDataIsSuccessfullyInsertedInTheTable() {
        // given
        String username = "abunjing";
        String password = "Passw0rd@123";

        String expectedResult = "abunjingPassw0rd@123";

        DatabaseConnection.CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS credentials (" +
                                                    "user_name VARCHAR(20) NOT NULL PRIMARY KEY," +
                                                    "password VARCHAR(20) NOT NULL);";
        DatabaseConnection databaseConnection = new DatabaseConnection();
        // when
        String userNameResult = Validation.getUserName(databaseConnection, username);
        String passwordResult = Validation.getPassword(databaseConnection, username);
        // then
        Assertions.assertEquals(expectedResult, userNameResult + passwordResult);
    }
}