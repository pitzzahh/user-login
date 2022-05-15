package com.pitzzahh.validation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import com.pitzzahh.database.DatabaseConnection;
import com.pitzzahh.exception.UserNotFoundException;
import com.pitzzahh.exception.UserAlreadyExistsException;

public class Validation {
    /**
     * Method that returns the username of the user.
     * @param databaseConnection database connection needed to connect to the database.
     * @param username the username needed to get the username of the user. (checks if the username exists in the table)
     * @return {@code username} of the user from the table
     */
    public static String getUserName(DatabaseConnection databaseConnection, String username) {
        try {
            ResultSet resultSet = databaseConnection.connect().createStatement().executeQuery(getUsernameQuery.apply(username));
            if (resultSet.next()) return resultSet.getString("user_name");
        } catch (SQLException sqlException) {
            throw new UserNotFoundException();
        }
        return "0";
    }

    /**
     * Function that validates if the username or password is valid.
     */
    public static Predicate<String> isUserNameOrPasswordValid = credential -> !credential.matches("[^a-z\\d^*&@]");

    /**
     * Function that validates if the user already exists in the table
     */
    public static BiPredicate<String, DatabaseConnection> doesUserNameExist = (username, databaseConnection) -> getUserName(databaseConnection, username).equals(username);

    /**
     * Function that returns a query for getting the username from the table.
     */
    public static Function<String, String> getUsernameQuery = username -> "SELECT user_name FROM credentials WHERE user_name = " + "'" + username + "'";

    /**
     * Method that returns the password of the user.
     * <p>Performs validation, it checks if the user is present in the column {@code user_name}
     * If the user_name is present in the column in the table it gets the user password then return it.
     * <p/>
     * @param databaseConnection database connection needed to connect to the database.
     * @param username the username needed to get the password of the user.
     * @return {@code password} of the user from the table
     */

    public static String getPassword(DatabaseConnection databaseConnection, String username) {
        try {
            ResultSet resultSet = databaseConnection.connect().createStatement().executeQuery(getPasswordQuery.apply(username));
            if (resultSet.next()) return resultSet.getString("password");
        } catch (SQLException sqlException) {
            throw new UserNotFoundException();
        }
        return "0";
    }

    /**
     * Function that returns a query for getting the password from the table.
     */
    public static Function<String, String> getPasswordQuery = username -> "SELECT password FROM credentials WHERE user_name = " + "'" + username + "'";

    /**
     * Method that inserts data from the table.
     *
     * @param databaseConnection database connection needed to connect to the database.
     * @param username the username needed to insert in the table, user_name column in the table.
     * @param password the password needed to insert in the table, password column in the table.
     * @throws UserAlreadyExistsException if the username already exists in the table, column user_name. (username is the primary key, it means it cannot be duplicated).
     */
    public static void insertData(DatabaseConnection databaseConnection, String username, String password) {
        String INSERT_STATEMENT = "INSERT INTO credentials (user_name, password) VALUES (?, ?);";
        try {
            PreparedStatement preparedStatement = databaseConnection.connect().prepareStatement(INSERT_STATEMENT);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            if (getUserName(databaseConnection, username).equals(username)) throw new UserAlreadyExistsException("User: " + username);
            System.out.println(sqlException.getMessage());
        }
    }

    /**
     * Method that returns {@code true} or {@code false} based on the {@code String} passed in.
     * @param numberString the {@code String} that might contain all numbers
     * @return {@code true} if the {@code String} can be parsed to an Integer.
     */
    public static boolean isNumber(String numberString) {
        try {
            Integer.parseInt(numberString);
            return true;
        } catch (Exception ignored) {}
        return false;
    }
}
