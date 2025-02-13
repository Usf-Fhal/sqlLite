import java.util.Scanner;

import Banner.SqlBanner;
import Parser.QueryParser;
import StorageEngine.StorageEngine;

public class SQLiteShell {
    private static boolean inSqlMode = false;

    public static void main(String[] args) {

        //Load required Ressources
        startDB();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the SQLite Shell. Type 'sql' to enter SQL mode, 'exit' to quit.");

        while (true) {
            System.out.print(inSqlMode ? "SQL> " : "> ");
            String input = scanner.nextLine().trim();

            if (!inSqlMode) {
                switch (input.toLowerCase()) {
                    case "sql":
                        inSqlMode = true;
                        System.out.println("Entered SQL mode. Type your SQL queries or 'exit' to leave.");
                        break;
                    case "exit":
                        System.out.println("Exiting the shell. Goodbye!");
                        return;
                    case "help":
                        System.out.println("Commands:\n - sql: Enter SQL mode\n - exit: Quit the shell\n - help: Show this message");
                        break;
                    default:
                        System.out.println("Unknown command. Type 'help' for available commands.");
                        break;
                }
            } else {
                if (input.equalsIgnoreCase("exit")) {
                    inSqlMode = false;
                    System.out.println("Exited SQL mode.");
                } else {
                    QueryParser.parseQuery(input);
                }
            }
        }
    }

    public static void startDB(){
        StorageEngine.loadStorageEngine();
        SqlBanner.banner();

    }

}
