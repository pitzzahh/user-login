package com.pitzzahh.database;

import java.sql.*;
import java.util.ArrayList;
import com.pitzzahh.entity.User;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import com.pitzzahh.exception.UserAlreadyExistsException;

public class DatabaseConnection {
    private static final String USERS_CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS credentials (" +
                                                               "user_name VARCHAR(20) NOT NULL PRIMARY KEY," +
                                                               "password VARCHAR(20) NOT NULL);";
    public DatabaseConnection() {
        try {
            Consumer<Connection> createTable = connection -> {
                try {
                    connection.prepareStatement(USERS_CREATE_TABLE_STATEMENT).executeUpdate();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            };
            createTable.accept(connect());
            System.out.println("Connected to the PostgreSQL Server Successfully.");
        } catch (RuntimeException runtimeException) {
            System.out.println(runtimeException.getMessage());
        }
    }

    /**
     * Method that connects to the PostgreSQL database.
     * @return a Connection object.
     */
    private Connection connect() {
        Connection connection = null;
        try {
            final String URL = "jdbc:postgresql://localhost:5432/peter";
            connection = DriverManager.getConnection(URL, "peter", "!Password123");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return connection;
    }

    /**
     * Method that inserts values into the table.
     * @param userName the username of a user to be inserted in the user_name column in the table.
     * @param password the password of a user to be inserted in the password column in the table.
     */
    public void insertValues(String userName, String password)  {
        final String INSERT_STATEMENT = "INSERT INTO credentials (user_name, password) VALUES (?, ?);";
        try {
            PreparedStatement preparedStatement = connect().prepareStatement(INSERT_STATEMENT);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
//            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(new UserAlreadyExistsException(userName).getMessage());
        } catch (NullPointerException ignored) {}
    }

    /**
     * Function that returns a query based on the given parameter table.
     */
    private static final Function<String, String> getAllData = tableName -> "SELECT * FROM " + tableName;

    /**
     * Function that returns a query based on the given parameter table.
     */
    private static final BiFunction<String, String, String> getUserPassword = (tableName, username) -> "SELECT password FROM " + tableName + " WHERE user_name = " + '\'' +  username + '\'';
    /**
     * Function that retrieves data from the table.
     * First argument is the number of rows to be retrieved.
     * Second argument is the name of the table on the com.pitzzahh.database.
     */
    public Function<String, ArrayList<User>> retrieveUsersData = tableName -> {
        ArrayList<User> arrayList = new ArrayList<>();
        try {
            Statement statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery(getAllData.apply(tableName));
            while (resultSet.next()) {
                arrayList.add(new User(
                        resultSet.getString("user_name"),
                        resultSet.getString("password")));
            }
            return arrayList;
        } catch (SQLException | NullPointerException exception) {
            System.out.println(exception.getMessage());
        }
        return new ArrayList<>();
    };
    public final Function<String, String> getPassword = username -> {
        try {
            Statement statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery(getUserPassword.apply("credentials", username));
            if (resultSet.next()) return resultSet.getString("password");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "0";
    };
}

