package com.tl.pokedex.util;

public class StringUtil {
    /**
     * Removes escape characters (like \n, \r, \t, \f, etc.) from the input string.
     *
     * @param input the string from which escape characters should be removed
     * @return a new string with all escape characters removed
     */
    public static String removeEscapeCharacters(String input) {
        if (input == null) return null;

        return input.replaceAll("\\s", " ").trim();
    }
}
