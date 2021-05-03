package edu.neu.mad_sea.yaofuyang.dictionary_classes;

public final class ParameterChecker {
    public static final int MAX_LETTERS = 10;
    public static final int MIN_LETTERS = 1;

    public static String checkInput(String letters, String pattern, String wordLength) {
        if (isEmpty(letters) || isEmpty(pattern) || isEmpty(wordLength)) {
            return "All three fields are required.";
        } else if (letters.length() > MAX_LETTERS) {
            return "The available letters cannot exceed " + MAX_LETTERS + " letters";
        } else if (!isAllLetters(letters)) {
            return "The available letters must be lowercase alphabet";
        } else if (!isCorrectPattern(pattern)) {
            return "The pattern must be underscores and/or lowercase alphabet";
        } else if (!isProperLength(wordLength)) {
            return "Word length must be a number between " + MIN_LETTERS + " and " + MAX_LETTERS;
        } else if (!isMatchingLength(pattern, wordLength)) {
            return "Length of pattern must match the specified word length.";
        } else if (!containsPattern(pattern, letters)) {
            return "Pattern letters must be included in available letters.";
        }
        return null;
    }

    private static boolean isAllLetters(String letters) {
        for (char letter : letters.toCharArray()) {
            if (letter < 'a' || letter > 'z') {
                return false;
            }
        }
        return true;
    }

    private static boolean isCorrectPattern(String pattern) {
        for (char character : pattern.toCharArray()) {
            if (character != '_') {
                if (character < 'a' || character > 'z') {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isProperLength(String wordLength) {
        try {
            int length = Integer.parseInt(wordLength);
            return length >= MIN_LETTERS && length <= MAX_LETTERS;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean isMatchingLength(String pattern, String wordLength) {
        try {
            int length = Integer.parseInt(wordLength);
            return length == pattern.length();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean containsPattern(String pattern, String letters) {
        for (char character : pattern.toCharArray()) {
            if (character != '_' && letters.indexOf(character) == -1) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(String param) {
        return param.equals("");
    }
}
