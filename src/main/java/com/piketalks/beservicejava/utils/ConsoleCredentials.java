package com.piketalks.beservicejava.utils;
import java.io.Console;
import java.util.Scanner;

public final class ConsoleCredentials {

    private static final Console console = System.console();
    private Scanner scanner;

    public String consoleReadUsername(){
        if (console != null)
            return new String(console.readPassword("Username: "));
        else{
            scanner = new Scanner(System.in);
            System.out.println("Username: ");
            return scanner.next();
        }
    }

    public String consoleReadPassword(){
        if (console != null)
            return new String(console.readPassword("Password: "));
        else{
            scanner = new Scanner(System.in);
            System.out.println("Password: ");
            return scanner.next();
        }
    }
}
