package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PhoneNumber {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "Select the way to input number:\n" +
                        "1. File input\n" +
                        "2. Console input"
        );
        int choice;
        try{
            choice = sc.nextInt();
        } catch(InputMismatchException e){
            System.out.println("Wrong input format.") ;
            return ;
        }
        switch (choice){
            case 1:
                processInputFromFile();
                break;
            case 2:
                processInputFromConsole();
                break;
            default:
                ;
        }
    }

    public static void processInputFromFile() {
        Scanner sc;
        try {
            sc = new Scanner(
                    new File(
                            System.getProperty("user.dir") + "\\res\\input.txt"
                    )
            );
        } catch (FileNotFoundException e) {
            System.out.println("File not found.") ;
            return;
        }
        String input ;
        try {
            input = sc.nextLine();
        }
        catch( InputMismatchException e ) {
            System.out.println("Input error.") ;
            return ;
        }
        if( isGood(input) )
            System.out.println("Correct number format.") ;
        else
            System.out.println("Wrong number format.") ;
    }

    public static void processInputFromConsole(){
        String input = "" ;
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter phone number: ");
            input = sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Input error.") ;
            return ;
        }
        if( isGood(input) )
            System.out.println("Correct number format.") ;
        else
            System.out.println("Wrong number format.") ;
    }

    public static boolean isGood(String number){
        return Pattern.compile(
                "^(\\+?[0-8] )?((\\([0-9]{3,4}\\) )|" +
                        "([0-9]{3,4} ))?(([0-9]{2,3} [0-9]{2,3} [0-9]{2,3})|" +
                        "([0-9]{2,3}-[0-9]{2,3}-[0-9]{2,3}))\r?\n?$"
        ).matcher(number).matches();
    }
}
