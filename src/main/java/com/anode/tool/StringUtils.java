package com.anode.tool;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for string manipulation and stream conversion operations.
 */
public class StringUtils {
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private StringUtils(){}


    /**
     * Counts the occurrences of a character in a string.
     * @param s the string to search
     * @param c the character to count
     * @return the number of times the character appears in the string
     */
    public static int getCount(String s, char c) {
        int count = 0;
        int index = 0;
        int fromIndex = 0;
    
        while (true) {
          index = s.indexOf(c, fromIndex);
    
          if (index == -1) {
            break;
          }
    
          count++;
    
          if (index == (s.length() - 1)) {
            break;
          }
    
          fromIndex = index + 1;
        }
    
        return count;
      }


    /**
     * Finds the index of the nth occurrence of a character in a string.
     * @param s the string to search
     * @param c the character to find
     * @param occurrence the occurrence number (1-based)
     * @param fromStart true to search from the start, false to search from the end
     * @return the index of the character, or -1 if not found
     */
    public static int getIndexOfChar(String s, char c, int occurrence, boolean fromStart) {
        if (s.isEmpty()) {
          return -1;
        }
    
        if (fromStart == true) {
          int count = 0;
          int index = 0;
          int fromIndex = 0;
    
          while (true) {
            index = s.indexOf(c, fromIndex);
    
            if (index == -1) {
              break;
            }
    
            count++;
    
            if (count == occurrence) {
              break;
            }
    
            fromIndex = index + 1;
          }
    
          return index;
        }
        else {
          int count = 0;
          int index = 0;
          int fromIndex = s.length() - 1;
    
          while (true) {
            index = s.lastIndexOf(c, fromIndex);
    
            if (index == -1) {
              break;
            }
    
            count++;
    
            if (count == occurrence) {
              break;
            }
    
            fromIndex = index - 1;
          }
    
          return index;
        }
      }


      /**
       * Checks if a string is null or contains only whitespace.
       * @param s the string to check
       * @return true if the string is null or empty after trimming
       */
      public static boolean isNullOrEmpty(String s) {
        if ((s == null) || (s.trim().isEmpty() == true)) {
          return true;
        }
        else {
          return false;
        }
      }


    /**
     * Reads an InputStream and converts it to a UTF-8 string.
     * @param inputStream the input stream to read
     * @return the string content of the stream
     * @throws RuntimeException if an I/O error occurs
     */
    public static String getStringFromStream(InputStream inputStream) {
    ByteArrayOutputStream result = new ByteArrayOutputStream();

    byte[] buffer = new byte[1024];
    int length;
    String s = null;

    try {
      while ((length = inputStream.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }

      s = result.toString(StandardCharsets.UTF_8.toString());
      result.close();
    }
    catch (IOException ex) {
      throw new RuntimeException("base_err_3", ex);
    }
    return s;
  }

  /**
   * Removes escape characters from a string.
   * @param s the string to process
   * @param ec the escape character
   * @param chars the characters to unescape
   * @return the processed string with escape characters removed
   */
  public static String removeEscapeChars(String s, char ec, char... chars) {
    StringBuffer sb = new StringBuffer(s.length());
    int size = s.length();

    for (int i = 0; i < size; i++) {
      if (i == (size - 1)) {
        sb.append(s.charAt(i));
      }
      else {
        char c = s.charAt(i);
        if (c == ec) {
          c = s.charAt(i + 1);
          if ((compareWithMany(c, chars) == true) || (c == ec)) {
                      // we do not need to copy the escape char
                    }
                    else {
                      sb.append(s.charAt(i));
                    }
                  }
                  else {
                    sb.append(s.charAt(i));
                  }
                }
              }
          
              return sb.toString().trim();
            }
          
          
            /**
             * Checks if a character equals any in a list of characters.
             * @param first the character to compare
             * @param others the array of characters to compare against
             * @return true if first matches any character in others
             */
            public static boolean compareWithMany(char first, char... others) {
              if (others == null) {
                return false;
              }
          
              for (int i = 0; i < others.length; i++) {
                if (first == others[i]) {
                  return true;
                }
              }
          
              return false;
            }


            /**
             * Adds escape characters to a string.
             * @param s the string to process
             * @param ec the escape character to prepend
             * @param chars the characters to escape
             * @return the processed string with escape characters added
             */
            public static String escapeChars(String s, char ec, char... chars) {
              StringBuffer sb = new StringBuffer(s.length() + 10); // abitrarily assuming that there will not be more than 10 characters required to be escaped
              int size = s.length();
          
              for (int i = 0; i < size; i++) {
                char c = s.charAt(i);
                if ((compareWithMany(c, chars) == true) || (c == ec)) {
                  sb.append(ec);
                }
                sb.append(c);
              }
              return sb.toString().trim();
            }
          


    /**
     * Loads a resource file from the classpath as a string.
     * @param clazz the class whose classloader will be used
     * @param filePath the path to the resource file
     * @return the content of the resource file, or null if not found
     * @throws RuntimeException if an I/O error occurs
     */
    public static String getResourceAsString(Class clazz, String filePath) {
      String s = null;
      InputStream is = clazz.getResourceAsStream(filePath);

      if (is == null) {
        return null;
      }

      InputStream bis = new BufferedInputStream(is);
      s = getStringFromStream(bis);
      try {
        bis.close();
      }
      catch (IOException ex) {
        throw new RuntimeException("base_err_3", ex);
      }
      return s;
    }


    /**
     * Checks if a string equals any in a list of strings.
     * @param first the string to compare
     * @param others the array of strings to compare against
     * @return true if first equals any string in others
     */
    public static boolean compareWithMany(String first, String... others) {
      if (others == null) {
        return false;
      }
  
      for (int i = 0; i < others.length; i++) {
        if (first.equals(others[i])) {
          return true;
        }
      }
  
      return false;
    }
  

}

