package edu.neu.mad_sea.yaofuyang.dictionary_classes;

import java.util.Arrays;

/**
 * Creates an instance of the TrieNode class which contains the following fields:
 * an array of TrieNode values representing pointers to its children, and a boolean
 * value representing if this node is the leaf node of a word.
 */
public class TrieNode {
    private static final int ALPHABET_NUMBER = 26;
    private TrieNode[] children = new TrieNode[ALPHABET_NUMBER];
    private boolean isLeaf;

    public TrieNode() {
        Arrays.fill(this.children, null);
        this.isLeaf = false;
    }

    public TrieNode[] getChildren() {
        return this.children;
    }

    public boolean isLeaf() {
        return this.isLeaf;
    }

    public void setLeaf(boolean leaf) {
        this.isLeaf = leaf;
    }

    public TrieNode getChild(int index) {
        return this.children[index];
    }

    @Override
    public String toString() {
        return "TrieNode{" +
                "children=" + Arrays.toString(children) +
                ", isLeaf=" + isLeaf +
                '}';
    }
}
