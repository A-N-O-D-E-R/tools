package com.anode.tool;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringUtils {
    private StringUtils(){}


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


      public static boolean isNullOrEmpty(String s) {
        if ((s == null) || (s.trim().isEmpty() == true)) {
          return true;
        }
        else {
          return false;
        }
      }


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

