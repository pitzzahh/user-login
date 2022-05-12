package com.pitzzahh;

import java.util.ArrayList;
import com.pitzzahh.database.*;
import com.pitzzahh.entity.User;
import java.util.function.Function;
import com.pitzzahh.exception.UserNotFoundException;

public class Test {
    public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            String password = databaseConnection.getPassword.apply("pass");
            if (password.equals("0")) throw new UserNotFoundException();
            else System.out.println("PASSWORD: " + password);
        } catch (RuntimeException runtimeException) {
            System.out.println(runtimeException.getMessage());
        }
    }
    public static void insertToTable(ArrayList<User> users, DatabaseConnection databaseConnection) {
        for (User user : users) {
            databaseConnection.insertValues(user.username(), user.password());
        }
    }
    public static Function<DatabaseConnection, ArrayList<User>> retrieveDataFromTable =
            databaseConnection -> databaseConnection.retrieveUsersData.apply("credentials");
}
