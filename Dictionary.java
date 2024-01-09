import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import HM1.SLL;
public class Dictionary {

    private AVLTree<String> myDictionary; // AVL Tree to maintain a balanced dictionary

    public Dictionary() {
        // Constructor to initialize an empty dictionary
        myDictionary = new AVLTree<>();
    }

    public Dictionary(String string) {
        // Constructor to initialize a dictionary with a single string
        this(); // Call the empty constructor to initialize the AVLTree
        myDictionary.insertAVL(string); // Insert the single string into the AVL Tree
    }

    public Dictionary(File file) {
        // Constructor to initialize the dictionary from a file
        this(); // Call the empty constructor to initialize the AVLTree
        try (Scanner fileReader = new Scanner(file)) {
            while (fileReader.hasNext()) {
                String word = fileReader.next();
                // Avoid duplicates by checking if the word is already in the tree
                if (!myDictionary.isInTree(word)) {
                    myDictionary.insertAVL(word); // Insert the word into the AVL Tree
                }
            }
        } catch (IOException e) {
            // Throw an unchecked exception if file reading fails
            throw new RuntimeException("Could not load dictionary from file: " + file, e);
        }
    }

    public void addWord(String s) throws WordAlreadyExistsException {
        // Add a word to the dictionary if it doesn't already exist
        if (!myDictionary.isInTree(s))
            myDictionary.insertAVL(s);
        else
            throw new WordAlreadyExistsException(s); // Throw exception if word exists
    }

    public boolean findWord(String s) {
        // Check if a word exists in the dictionary
        return myDictionary.isInTree(s); // Return true if found, false otherwise
    }

    public void deleteWord(String s) throws WordNotFoundException {
        // Delete a word from the dictionary if it exists
        if (!myDictionary.isInTree(s))
            throw new WordNotFoundException(s); // Throw exception if word doesn't exist
        else
            myDictionary.deleteAVL(s); // Delete the word from the AVL Tree
    }

    public String[] findSimilar(String s) {
        // Find words in the dictionary that are similar to the given word
        if (this.myDictionary.isEmpty()) {
            throw new RuntimeException("Dictionary is empty"); // Throw if dictionary is empty
        }
        SLL<String> list = new SLL<>(); // Create a linked list to store similar words
        visitNodes(myDictionary.root, list, s); // Traverse the AVL tree to find similar words
        String[] similarArray = new String[list.size()]; // Convert linked list to array
        int i = 0;
        while (!list.isEmpty()) {
            similarArray[i] = list.deleteFromHead(); // Populate the array with similar words
            i++;
        }
        return similarArray;
    }

    // Helper method to traverse the AVL tree and find similar words
    private void visitNodes(BSTNode<String> node, SLL<String> list, String s) {
        if (node != null) {
            visitNodes(node.left, list, s); // Visit left subtree
            if (isSimilar(s, node.el)) {
                list.addToTail(node.el); // Add the node to the list if it's similar
            }
            visitNodes(node.right, list, s); // Visit right subtree
        }
    }

    // Helper method to determine if two strings are similar
    private boolean isSimilar(String str1, String str2) {
        if (str1.equals(str2)) return false; // Strings must not be identical
        if (Math.abs(str1.length() - str2.length()) > 1) return false; // Length difference must be 0 or 1

        int diffCount = 0; // Counter for the number of differences
        for (int i = 0, j = 0; i < str1.length() && j < str2.length(); i++, j++) {
            
            if (str1.charAt(i) != str2.charAt(j)) {
                diffCount++; // Increment the counter for each difference
                // Adjust indexes for strings of different lengths

                if (str1.length() > str2.length()) j--;
                else if (str1.length() < str2.length()) i--;

                if (diffCount > 1) return false; // More than one difference is not allowed
            }
        }

        return diffCount == 1 || Math.abs(str1.length() - str2.length()) == 1; // Only one difference is allowed
    }

    public void saveDictionary(String filename) throws IOException {
        // Save the dictionary to a file
        File file = new File(filename);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            saveTreeToFile(myDictionary.root, writer); // In-order traversal to save words
        }
    }

    // Helper method to save the AVL tree words to a file in-order
    private void saveTreeToFile(BSTNode<String> node, PrintWriter writer) {
        if (node != null) {
            saveTreeToFile(node.left, writer); // Traverse left subtree
            writer.println(node.el); // Write the word to the file
            saveTreeToFile(node.right, writer); // Traverse right subtree
        }
    }
}

// exception class for when a word already exists in the dictionary
class WordAlreadyExistsException extends Exception {
    public WordAlreadyExistsException(String message) {
        super(message);
    }
}

// exception class for when a word is not found in the dictionary
class WordNotFoundException extends Exception {
    public WordNotFoundException(String message) {
        super(message);
    }
}

