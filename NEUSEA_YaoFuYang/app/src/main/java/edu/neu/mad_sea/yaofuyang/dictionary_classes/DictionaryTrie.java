package edu.neu.mad_sea.yaofuyang.dictionary_classes;

import java.util.ArrayList;

public class DictionaryTrie {
    private TrieNode root;
    private ArrayList<Result> searchRes;
    private long searchTime;
    private static final int NANO_TO_MICRO = 1000;

    /**
     * Constructor for an instance of the DictionaryTrie data structure.
     */
    public DictionaryTrie() {
        this.root = new TrieNode();
        this.searchRes = new ArrayList<>();
    }

    /**
     * Given a String representing a word, adds to the internal data structure.
     * @param word - String representing the word to add into the data structure
     */
    public void insert(String word) {
        TrieNode letterTracker = this.root;  // Another pointer to the root.
        int length = word.length();
        int childIndex;

        for (int i = 0; i < length; i++) {
            childIndex = word.charAt(i) - 'a';
            if (letterTracker.getChild(childIndex) == null) {
                letterTracker.getChildren()[childIndex] = new TrieNode();
            }

            letterTracker = letterTracker.getChild(childIndex);
        }

        // Mark last node as a leaf node.
        letterTracker.setLeaf(true);
    }

    /**
     * Given a String representing the search word, returns true if the word exists inside
     * the trie data structure and false otherwise.
     * @param word - String representing the search word.
     * @return true if the word exists in the data structure and false otherwise.
     */
    public boolean search(String word) {
        TrieNode letterTracker = this.root;
        int length = word.length();
        int childIndex;

        for (int j = 0; j < length; j++) {
            childIndex = word.charAt(j) - 'a';
            if (letterTracker.getChild(childIndex) == null) {
                return false;
            }
            letterTracker = letterTracker.getChild(childIndex);
        }

        return (letterTracker != null && letterTracker.isLeaf());
    }

    public ArrayList<Result> customSearch(String letters, String pattern) {
        long start = System.nanoTime();
        StringBuilder potentialLetters = new StringBuilder(letters);
        StringBuilder wordPattern = new StringBuilder(pattern);

        // Updated potential letters to only include non-specified letters.
        for (int i = 0; i < pattern.length(); i++) {
            for (int j = 0; j < potentialLetters.length(); j++) {
                if (pattern.charAt(i) == potentialLetters.charAt(j)) {
                    potentialLetters.deleteCharAt(j);
                    break;
                }
            }
        }

        recursiveSearch(this.root, potentialLetters, wordPattern, new StringBuilder());
        long end = System.nanoTime();
        this.searchTime = end - start;

        System.out.println(this.searchRes.toString());
        return this.searchRes;
    }

    public String getSearchTime() {
        return (this.searchTime/NANO_TO_MICRO) + "\u00B5"+"s";
    }

    private void recursiveSearch(TrieNode node, StringBuilder letters, StringBuilder pattern, StringBuilder word) {
        if (pattern.toString().length() == 0) {
            if (node.isLeaf()) {
                this.searchRes.add(new Result(word.toString()));  // Only adds marked words.
            }
//            System.out.println("This is the word at base case: " + word + "\n");
            return;
        }
        char singleChar = pattern.charAt(0);
        if (singleChar == '_') {  // Wildcard case
//            System.out.println("Reached the wild-card case\n");
            for (int i = 0; i < letters.length(); i++) {
                char currentChar = letters.charAt(i);
                TrieNode child = node.getChild(currentChar - 'a');
                if (child != null) {  // Has value
                    // Build new StringBuilders
                    StringBuilder newWord = new StringBuilder(word.toString() + currentChar);
                    StringBuilder newLetters = new StringBuilder(letters);
                    StringBuilder newPattern = new StringBuilder(pattern.toString());

                    // Modify necessary parameters.
                    newLetters.deleteCharAt(i);
                    newPattern.deleteCharAt(0);
//                    System.out.println("New available letters: " + newLetters.toString() + "\n");
//                    System.out.println("New pattern: " + pattern.toString() + "\n");
//                    System.out.println("New word: " + newWord.toString() + "\n");

                    // Recursive call with updated parameters.
                    recursiveSearch(child, newLetters, newPattern, newWord);  // Call with specific pattern.
                }
            }
        } else {  // Specific letter case.
            TrieNode child = node.getChild(singleChar - 'a');
            if (child != null) {
//                System.out.println("Reached the specific letter case.\n");
                word.append(singleChar);
                pattern.deleteCharAt(0);
                recursiveSearch(child, letters, pattern, word);
            }
        }
    }

    @Override
    public String toString() {
        return "DictionaryTrie{" +
                "root=" + root.toString() +
                '}';
    }
}
