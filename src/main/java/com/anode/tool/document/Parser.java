package com.anode.tool.document;

import java.util.ArrayList;
import java.util.List;

import com.anode.tool.StringUtils;



/**
 * Parser for converting path strings into token sequences.
 */
class Parser {

    /**
     * Parses a path string into a list of tokens.
     * @param path the path string to parse
     * @return a list of tokens representing the path
     */
    public static List<Token> getTokens(String path) {
    List<String> strTokens = getStringTokens(path);
    List<Token> tokens = getTokens(strTokens);
    return tokens;
  }

    /**
     * Converts a list of string tokens to Token objects.
     * @param strTokens the string tokens to convert
     * @return a list of Token objects
     */
    private static List<Token> getTokens(List<String> strTokens) {
    List<Token> tokens = new ArrayList<>();
    int size = strTokens.size();

    for (int i = 0; i < size; i++) {
      String strToken = strTokens.get(i);
      boolean isLeaf = false;

      if (i == (size - 1)) {
        isLeaf = true;
      }

      int first = isPresent(strToken, '[');
      if (first != -1) {
        tokens.add(getArrayToken(strToken, first, isLeaf));
      }
      else {
        String s = StringUtils.removeEscapeChars(strToken, '\\', '.', '[', ']', '=');
        tokens.add(new Token(s, isLeaf));
      }
    }

    return tokens;
  }

    /**
     * Parses an array token from a string.
     * @param s the string to parse
     * @param first the index of the first bracket
     * @param isLeaf whether this token is a leaf node
     * @return the parsed ArrayToken
     */
    private static ArrayToken getArrayToken(String s, int first, boolean isLeaf) {
    ArrayToken at = null;
    String name = StringUtils.removeEscapeChars(s.substring(0, first), '\\', '.', '[', ']', '=');

    while (true) {
      if (s.charAt(first + 1) == ']') {
        // it is a empty array token
        at = new ArrayToken(name, isLeaf);
        break;
      }

      {
        int pos = s.lastIndexOf(']');
        s = s.substring(first + 1, pos);
        pos = isPresent(s, '=');
        if (pos != -1) {
          // it is a key value pair
          String key = StringUtils.removeEscapeChars(s.substring(0, pos), '\\', '.', '[', ']', '=');
          String value = StringUtils.removeEscapeChars(s.substring(pos + 1), '\\', '.', '[', ']', '=');
          at = new ArrayToken(name, key, value, isLeaf);
        }
        else {
          // it is an index
          s = StringUtils.removeEscapeChars(s, '\\', '.', '[', ']', '=');
          at = new ArrayToken(name, Integer.parseInt(s), isLeaf);
        }
        break;
      }
    }

    return at;
  }

    /**
     * Checks if a symbol is present in a string (ignoring escaped symbols).
     * @param s the string to search
     * @param symbol the symbol to find
     * @return the index of the symbol, or -1 if not found
     */
    private static int isPresent(String s, char symbol) {
    int pos = -1;

    int start = 0;
    while (true) {
      if (start >= s.length()) {
        break;
      }
      int i = s.indexOf(symbol, start);
      if (i != -1) {
        if (isEscaped(s, i, '\\') == false) {
          pos = i;
          break;
        }
        else {
          start = i + 1;
        }
      }
      else {
        break;
      }
    }

    return pos;
  }

    /**
     * Splits a path string into string tokens.
     * @param s the path string to split
     * @return a list of string tokens
     */
    private static List<String> getStringTokens(String s) {
    List<String> paths = new ArrayList<>();
    int from = 2;

    for (int i = 2; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '.') {
        if (isEscaped(s, i, '\\') == false) {
          paths.add(s.substring(from, i));
          from = i + 1;
        }
      }
    }

    if (from < s.length()) {
      paths.add(s.substring(from));
    }

    return paths;
  }

    /**
     * Checks if a character at a position is escaped.
     * @param s the string to check
     * @param pos the position to check
     * @param ec the escape character
     * @return true if the character is escaped
     */
    private static boolean isEscaped(String s, int pos, char ec) {
    if (pos == 0) {
      return false;
    }

    char c = s.charAt(pos - 1);
    if (c == ec) {
      return true;
    }
    else {
      return false;
    }
  }

}
