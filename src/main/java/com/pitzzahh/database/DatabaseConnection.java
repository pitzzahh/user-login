package com.pitzzahh.database;

import java.sql.*;
import java.util.function.*;

public class DatabaseConnection {

    public static String TABLE_NAME;
    public static String CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS credentials (" +
                                                  "user_name VARCHAR(15) NOT NULL PRIMARY KEY," +
                                                  "password VARCHAR(20) NOT NULL);";
    public DatabaseConnection() {
        try {
            Consumer<Connection> createTable = connection -> {
                try {
                    TABLE_NAME = getTableName(CREATE_TABLE_STATEMENT);
                    connection.prepareStatement(CREATE_TABLE_STATEMENT).executeUpdate();
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
    public Connection connect() {
        Connection connection = null;
        try {
            final String DATABASE = "peter";
            final String USERNAME = "peter";
            final String PASSWORD = "!Password123";
            final String URL = "jdbc:postgresql://localhost:5432/" + DATABASE;
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return connection;
    }

    /**
     * Function that returns a query based on the given parameter table.
     */
    private static final Function<String, String> getAllDataQuery = tableName -> "SELECT * FROM " + tableName;

    /**
     * Method that gets the table name from the {@code CREATE_TABLE_STATEMENT}
     * @param statement the {@code CREATE_TABLE_STATEMENT}
     * @return the name of the table in the database.
     */
    private static String getTableName(String statement) {
        int[] subStrings = {statement.contains("EXISTS") ? statement.indexOf("EXISTS") + 6 : statement.indexOf("exists") + 6, statement.indexOf("(") - 1};
        return statement.substring(subStrings[0], subStrings[1]).trim();
    }

    /**
     * Method that gets all the columns in the table.
     * @return {@code String[]} an array containing all the columns of the table.
     */
    public String[] getAllColumn() {
        try {
            System.out.println("List of column names in the current table: " + TABLE_NAME);
            ResultSetMetaData resultSetMetaData = connect().createStatement().executeQuery(getAllDataQuery.apply(TABLE_NAME)).getMetaData();
            int count = resultSetMetaData.getColumnCount();
            String[] columns = new String[count];
            for(int i = 1; i <= count; i++) {
                columns[i - 1] = resultSetMetaData.getColumnName(i);
            }
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

