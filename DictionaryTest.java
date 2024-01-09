import java.io.*;
import java.util.Scanner;

public class DictionaryTest {

    public static void main(String[] args) {
        // Set up a scanner for user input
        Scanner scanner = new Scanner(System.in);
        Dictionary dictionary = null;

        // Prompt user for the dictionary file name
        System.out.print("Enter filename> ");
        String filename = scanner.nextLine().trim();
        
        // Remove quotes from the filename if present
        if (filename.startsWith("\"") && filename.endsWith("\"")) {
            filename = filename.substring(1, filename.length() - 1);
        }
        
        // Try to load the dictionary from the file
        try {
            dictionary = new Dictionary(new File(filename));
            System.out.println("Dictionary loaded successfully.");
        } catch (Exception e) {
            // Handle any exceptions during dictionary loading
            System.out.println("Failed to load dictionary: " + e.getMessage());
            return; // Exit if dictionary cannot be loaded
        }

        // Main command loop
        String command;
        while (true) {
            // Get user command
            System.out.print("Enter command> ");
            command = scanner.nextLine().trim().toLowerCase();

            // Display menu if user enters 'menu'
            if (command.equals("menu")) {
                printMenu();
                continue;
            }

            // Exit command to terminate the program
            if (command.equals("exit")) {
                // Prompt to save changes before exiting
                System.out.print("Save changes before exiting? (Yes/No) ");
                String response = scanner.nextLine().trim();
                if (response.equals("Yes")) {
                    try {
                        // Save the dictionary to a file
                        dictionary.saveDictionary(filename);
                        System.out.println("Changes saved successfully.");
                    } catch (IOException e) {
                        // Handle any exceptions during saving
                        System.out.println("Failed to save changes: " + e.getMessage());
                    }
                }
                break; // Exit the loop to terminate the program
            }

            // Execute the command entered by the user
            executeCommand(command, dictionary, scanner);
        }

        // Close the scanner resource
        scanner.close();
    }

    // Method to handle command execution logic
    private static void executeCommand(String command, Dictionary dictionary, Scanner scanner) {
        try {
            // Command 'find' to check if a word exists in the dictionary
            if (command.equals("find")) {
                System.out.print("Enter the word> ");
                String word = scanner.nextLine();
                boolean found = dictionary.findWord(word);
                System.out.println(found ? "Word found." : "Word not found.");
            } 
            // Command 'add' to add a new word to the dictionary
            else if (command.equals("add")) {
                System.out.print("Enter the word> ");
                String word = scanner.nextLine();
                dictionary.addWord(word);
                System.out.println("Word added successfully.");
            } 
            // Command 'remove' to delete a word from the dictionary
            else if (command.equals("remove")) {
                System.out.print("Enter the word> ");
                String word = scanner.nextLine();
                dictionary.deleteWord(word);
                System.out.println("Word removed successfully.");
            } 
            // Command 'search' to find similar words in the dictionary
            else if (command.equals("search")) {
                System.out.print("Enter the word> ");
                String word = scanner.nextLine();
                String[] similarWords = dictionary.findSimilar(word);
                if (similarWords.length == 0) {
                    System.out.println("No similar words found.");
                } else {
                    for (String str : similarWords) {
                        System.out.print(str + " ");
                    }
                    System.out.println();
                }
            } else {
                // Notify user of invalid command
                System.out.println("Invalid command. Type 'menu' to see available commands.");
            }
        } 
        // Catch specific exceptions and handle them
        catch (WordAlreadyExistsException e) {
            System.out.println("Exception: Word already exists.");
        } catch (WordNotFoundException e) {
            System.out.println("Exception: Word not found.");
        } catch (Exception e) {
            // Catch all other exceptions
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    // Method to print the application menu
    private static void printMenu() {
        // The menu layout for the user to see available commands
        System.out.println("\n           ** Dictionary Application Menu **       ");
        System.out.println(" -----------------------------------------------------");
        System.out.println("|  [find]   - Find a specific word in the dictionary  |");
        System.out.println("|  [add]    - Add a word to the dictionary            |");
        System.out.println("|  [remove] - Remove a word from the dictionary       |");
        System.out.println("|  [search] - Search for similar words                |");
        System.out.println("|  [exit]   - Exit the application                    |");
        System.out.println("|  [menu]   - Display this menu again                 |");
        System.out.println(" -----------------------------------------------------");
    }
}




