package ru.masnaviev.habittracker.util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHandler {

    private static final Scanner scanner = new Scanner(System.in);

    public static int getUserInputInt() {
        while (true) {
            try {
                int n = scanner.nextInt();
                scanner.nextLine();
                return n;
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Попробуйте снова");
                scanner.nextLine();
            }
        }
    }

    public static String getUserInputString() {
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                System.out.println("Некорректный ввод. Попробуйте снова");
                return getUserInputString();
            }
        } catch (Exception e) {
            System.out.println("Некорректный ввод. Попробуйте снова");
            return getUserInputString();
        }
    }
}
