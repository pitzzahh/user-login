package com.pitzzahh.app;

import java.io.*;
import java.awt.*;
import java.net.URI;
import java.util.Scanner;
import jdk.jfr.Description;
import com.pitzzahh.exception.*;
import com.pitzzahh.entity.Color;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import com.pitzzahh.validation.Validation;
import com.pitzzahh.database.DatabaseConnection;

/**
 * @author Peter John Arao
 */
@Description("jdk version: 17")
public class UserLogin {
    private static final DatabaseConnection databaseConnection = new DatabaseConnection();
    public static void main(String[] args) throws IOException, InterruptedException {
        mainSelection(new Scanner(System.in));
        System.out.println(Color.BLUE + "THANK YOU " + Color.YELLOW + "FOR USING " + Color.PURPLE + "MY PROGRAM");
    }

    /**
     * Method that asks the user if the user want's to log in or register or exit the program.
     * @param keyboardInput scanner needed for user input.
     * @throws IOException if one of the files or directory does not exist.
     */
    private static void mainSelection(Scanner keyboardInput) throws IOException, InterruptedException {
        while (true) {
            try {
                System.out.println(Color.PURPLE + """
                        \s
                        ██╗   ██╗███████╗███████╗██████╗     ██╗      ██████╗  ██████╗ ██╗███╗   ██╗
                        ██║   ██║██╔════╝██╔════╝██╔══██╗    ██║     ██╔═══██╗██╔════╝ ██║████╗  ██║
                        ██║   ██║███████╗█████╗  ██████╔╝    ██║     ██║   ██║██║  ███╗██║██╔██╗ ██║
                        ██║   ██║╚════██║██╔══╝  ██╔══██╗    ██║     ██║   ██║██║   ██║██║██║╚██╗██║
                        ╚██████╔╝███████║███████╗██║  ██║    ███████╗╚██████╔╝╚██████╔╝██║██║ ╚████║
                         ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝    ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═══╝ \s
                        """);
                final String LETTER_A = Color.BLUE + " A " + Color.RESET;
                final String LETTER_B = Color.YELLOW + " B " + Color.RESET;
                final String LETTER_C = Color.RED + " C " + Color.RESET;

                System.out.println(Color.YELLOW + ":" + LETTER_A + Color.YELLOW + ":" + Color.BLUE + " Login");
                System.out.println(Color.YELLOW + ":" + LETTER_B + Color.YELLOW + ":" + Color.YELLOW + " Register");
                System.out.println(Color.YELLOW + ":" + LETTER_C + Color.YELLOW + ":" + Color.RED + " Exit");
                System.out.print(Color.PURPLE + ">>: ");
                String choice = keyboardInput.nextLine().toUpperCase().trim();
                if (choice.equals("A") || choice.equals("B")|| choice.equals("C")) {
                    if (choice.equals("A")) {
                        if (login(keyboardInput)) {
                            System.out.println(Color.BLUE + "SUCCESSFULLY LOGGED IN" + Color.RESET);
                            loading();
                            Desktop.getDesktop().browse(new URI("https://youtu.be/q-Y0bnx6Ndw"));
                        } else throw new InvalidUserNameOrPasswordException("INVALID USERNAME OR PASSWORD");
                    }
                    if (choice.equals("B")) registration(keyboardInput);
                    if (choice.equals("C")) break;
                }
                else {
                    if (choice.isEmpty()) throw new BlankResponseException("BLANK CHOICE IS NOT ALLOWED");
                    else if (Validation.isNumber(choice)) throw new NumberResponseException("NUMBER RESPONSE IS NOT ALLOWED");
                    else if (Validation.isUserNameOrPasswordValid.negate().test(choice)) throw new SpecialCharacterResponseException("SPECIAL CHARACTER RESPONSE NOT ALLOWED");
                    throw new InvalidLetterResponseException("PLEASE CHOOSE:       A or B or C       ONLY");
                }
            } catch (RuntimeException runtimeException) {
                System.out.println(Color.RED + runtimeException.getMessage() + Color.RESET);
                loading();
            } catch (URISyntaxException | InterruptedException exception) {
                System.out.println(Color.RED + exception.getMessage() + Color.RESET);
            }
        }
    }

    /**
     * Method that creates a new user, checks if the username and password
     * contains invalid or special characters. Also checks if the user already exists.
     * Creates a folder for the user, inside the folder is the user credentials.
     * User credentials are also added to the records.txt
     * @param keyboardInput scanner needed for user input.
     */
    private static void registration(Scanner keyboardInput) throws InterruptedException {
        try {
            String username;
            String password;
            System.out.println(Color.YELLOW + """
                    ██████╗ ███████╗ ██████╗ ██╗███████╗████████╗███████╗██████╗\s
                    ██╔══██╗██╔════╝██╔════╝ ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
                    ██████╔╝█████╗  ██║  ███╗██║███████╗   ██║   █████╗  ██████╔╝
                    ██╔══██╗██╔══╝  ██║   ██║██║╚════██║   ██║   ██╔══╝  ██╔══██╗
                    ██║  ██║███████╗╚██████╔╝██║███████║   ██║   ███████╗██║  ██║
                    ╚═╝  ╚═╝╚══════╝ ╚═════╝ ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝\s
                    """);
            System.out.print(Color.GREEN + "Enter Username: ");
            username = keyboardInput.nextLine().trim();
            System.out.print("Enter Password: " + Color.RESET);
            password = keyboardInput.nextLine().trim();
            if (Validation.isUserNameOrPasswordValid.negate().test(username) ||
                Validation.isUserNameOrPasswordValid.negate().test(password)) throw new InvalidUserNameOrPasswordException("ALPHANUMERICAL CHARACTERS ONLY");
            else {
                if (Validation.doesUserNameExist.test(username, databaseConnection)) throw new UserAlreadyExistsException("USER: " + username);
                Validation.insertData(databaseConnection, username, password);
                System.out.println(Color.BLUE + "REGISTERED SUCCESSFULLY" + Color.RESET);
                loading();
            }
        } catch (RuntimeException runtimeException) {
            System.out.println(Color.RED + runtimeException.getMessage() + Color.RESET);
            loading();
        }
    }

    /**
     * Method that asks for user input on their credentials.
     * @param keyboardInput scanner needed for user input.
     * @return {@code true} if the inputted credentials matches the real credentials.
     */
    private static boolean login(Scanner keyboardInput) throws UserNotFoundException {
        System.out.println(Color.BLUE + """
                \s
                ██╗      ██████╗  ██████╗ ██╗███╗   ██╗
                ██║     ██╔═══██╗██╔════╝ ██║████╗  ██║
                ██║     ██║   ██║██║  ███╗██║██╔██╗ ██║
                ██║     ██║   ██║██║   ██║██║██║╚██╗██║
                ███████╗╚██████╔╝╚██████╔╝██║██║ ╚████║
                ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═══╝\s
                """);
        System.out.print(Color.GREEN + "Enter Username: ");
        String username = keyboardInput.nextLine().trim();
        System.out.print("Enter Password: " + Color.RESET);
        String password = keyboardInput.nextLine().trim();
        try {
            if (Validation.doesUserNameExist.negate().test(username, databaseConnection)) {
                throw new UserNotFoundException();
            }
            return (Validation.getUserName(databaseConnection, username).equals(username) && Validation.getPassword(databaseConnection, username).equals(password));
        } catch (RuntimeException runtimeException) {
            System.out.println(Color.RED + runtimeException.getMessage() + Color.RESET);
        }
        return false;
    }

    /**
     * Method that prints a trailing dot after a print text.
     * @throws InterruptedException if the thread is interrupted.
     */
    private static void loading() throws InterruptedException {
        System.out.print("REDIRECTING");
        for (int i = 0; i < 3; i++) {
            TimeUnit.MILLISECONDS.sleep(600);
            System.out.print(".");
        }
        System.out.println();
    }
}