package Client.Managers;

import java.util.Scanner;

public class ScannerManager {

    private final Scanner scanner;

    public ScannerManager() {
        scanner = new Scanner(System.in);
    }

    public String readCommand() {
        System.out.print("> ");
        return scanner.nextLine().trim();
    }

    public Scanner getScanner() {
        return scanner;
    }
}