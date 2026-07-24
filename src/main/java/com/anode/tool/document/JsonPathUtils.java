package com.anode.tool.document;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.anode.tool.StringUtils;

/**
 * Utility class for analyzing and manipulating JSON paths in documents and files.
 */
public class JsonPathUtils {

    /**
     * Regular expression pattern for matching JSON paths.
     */
    private static final String pattern = "\\$\\.[a-zA-Z \\-\\[=%\\]\\.0-9_]+";

    /**
     * Compiled pattern matcher for JSON paths.
     */
    private static final Matcher matcher = Pattern.compile(pattern).matcher("");

    /**
     * Finds and prints all unused paths from a document in a directory.
     * @param filePath the path to the JSON document file
     * @param dirPath the directory to search for path usages
     * @param filePattern the file pattern to match
     * @throws IOException if an I/O error occurs
     */
    public void getUnusedPaths(String filePath, String dirPath, String filePattern) throws IOException {
    Set<String> unusedPaths = new HashSet<>();
    getUniquePaths(filePath).forEach(unusedPaths::add);
    getUnusedPaths(unusedPaths, dirPath, filePattern);
    unusedPaths.forEach(System.out::println);
  }

    /**
     * Finds and prints all used paths from a document in a directory.
     * @param filePath the path to the JSON document file
     * @param dirPath the directory to search for path usages
     * @param filePattern the file pattern to match
     * @throws IOException if an I/O error occurs
     */
    public void getUsedPaths(String filePath, String dirPath, String filePattern) throws IOException {
    Set<String> usedPaths = new HashSet<>();
    getUniquePaths(filePath).forEach(usedPaths::add);
    getUsedPaths(usedPaths, dirPath, filePattern);
    usedPaths.forEach(System.out::println);
  }

    /**
     * Removes paths that are found in files matching the pattern.
     * @param paths the set of paths to check
     * @param baseDirPath the base directory to search
     * @param filePattern the file pattern to match
     * @throws IOException if an I/O error occurs
     */
    private void getUnusedPaths(Set<String> paths, String baseDirPath, String filePattern) throws IOException {
    try (Stream<Path> walk = Files.walk(Paths.get(baseDirPath))) {
      List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(filePattern)).collect(Collectors.toList());
      result.forEach(s -> {
        try {
          System.out.println("Processing file -> " + s);
          removeUnused(paths, s);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

    /**
     * Finds paths that are used in files matching the pattern.
     * @param paths the set of paths to check
     * @param baseDirPath the base directory to search
     * @param filePattern the file pattern to match
     * @throws IOException if an I/O error occurs
     */
    private void getUsedPaths(Set<String> paths, String baseDirPath, String filePattern) throws IOException {
    try (Stream<Path> walk = Files.walk(Paths.get(baseDirPath))) {
      Set<String> usedPaths = new HashSet<>();
      List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(filePattern)).collect(Collectors.toList());
      result.forEach(s -> {
        try {
          System.out.println("Processing file -> " + s);
          getUsedPaths(paths, s, usedPaths);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      });
      paths.clear();
      paths.addAll(usedPaths);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

    /**
     * Extracts used paths from a file.
     * @param paths the set of paths to check
     * @param fileName the file to search
     * @param usedPaths the set to collect used paths
     * @throws IOException if an I/O error occurs
     */
    private void getUsedPaths(Set<String> paths, String fileName, Set<String> usedPaths) throws IOException {
    Stream<String> lines = Files.lines(Paths.get(fileName));
    lines.forEach(s -> checkLine(paths, s, usedPaths));
  }

    /**
     * Removes used paths from the set.
     * @param paths the set of paths to filter
     * @param fileName the file to search
     * @throws IOException if an I/O error occurs
     */
    private void removeUnused(Set<String> paths, String fileName) throws IOException {
    Stream<String> lines = Files.lines(Paths.get(fileName));
    lines.forEach(s -> checkLine(paths, s));
  }

    /**
     * Removes paths found in a line from the set.
     * @param paths the set of paths to filter
     * @param line the line to search
     */
    private void checkLine(Set<String> paths, String line) {
    matcher.reset(line);
    while (matcher.find()) {
      String path = line.substring(matcher.start(), matcher.end());
      path = path.replaceAll("\\[[a-zA-Z0-9 \\-=_%]+\\]", "[]");
      if (paths.contains(path) == true) {
        paths.remove(path);
      }
    }
  }

    /**
     * Adds paths found in a line to the usedPaths set.
     * @param paths the set of paths to check
     * @param line the line to search
     * @param usedPaths the set to collect used paths
     */
    private void checkLine(Set<String> paths, String line, Set<String> usedPaths) {
    matcher.reset(line);
    while (matcher.find()) {
      String path = line.substring(matcher.start(), matcher.end());
      path = path.replaceAll("\\[[a-zA-Z0-9 \\-=_%]+\\]", "[]");
      if (paths.contains(path) == true) {
        usedPaths.add(path);
      }
    }
  }

    /**
     * Flattens a JSON document to a list of paths.
     * @param filePath the path to the JSON file
     * @return a list of all paths in the document
     * @throws IOException if the file cannot be read
     */
    public List<String> flattenPaths(String filePath) throws IOException {
    List<String> list = new LinkedList<>();
    InputStream is = new BufferedInputStream(new FileInputStream(filePath));
    String json = StringUtils.getStringFromStream(is);
    Document d = new JDocument(json);
    return d.flatten();
  }

    /**
     * Flattens a JSON document to a list of paths with their values.
     * @param filePath the path to the JSON file
     * @return a list of path-value pairs
     * @throws IOException if the file cannot be read
     */
    public List<PathValue> flattenPathsWithValues(String filePath) throws IOException {
    List<String> list = new LinkedList<>();
    InputStream is = new BufferedInputStream(new FileInputStream(filePath));
    String json = StringUtils.getStringFromStream(is);
    Document d = new JDocument(json);
    List<PathValue> pathValues = d.flattenWithValues();
    return pathValues;
  }

    /**
     * Gets unique paths from a JSON document (generalizing array indexes).
     * @param filePath the path to the JSON file
     * @return a list of unique paths
     * @throws IOException if the file cannot be read
     */
    public List<String> getUniquePaths(String filePath) throws IOException {
    List<String> list = flattenPaths(filePath);
    return getUniquePaths(list);
  }

    /**
     * Gets unique paths from a document.
     * @param d the document to process
     * @return a list of unique paths
     * @throws IOException if an error occurs
     */
    public List<String> getUniquePaths(Document d) throws IOException {
    List<String> list = d.flatten();
    return getUniquePaths(list);
  }

    /**
     * Deduplicates paths by replacing numeric array indexes with empty brackets.
     * @param flattenedPaths the list of paths with numeric indexes
     * @return a list of unique paths
     */
    private List<String> getUniquePaths(List<String> flattenedPaths) {
    Map<String, String> map = new HashMap<>();

    for (String s : flattenedPaths) {
      s = s.replaceAll("\\[\\d*+\\]", "[]");
      map.put(s, s);
    }

    List<String> list = new LinkedList<>();
    Set<String> keys = map.keySet();
    for (String key : keys) {
      list.add(key);
    }

    return list;
  }

    /**
     * Pads array indexes in paths with leading zeros to a width of 6 digits.
     * @param paths the list of paths with numeric indexes
     * @return a list of paths with zero-padded indexes
     */
    public static List<String> getZeroPaddedIndexes(List<String> paths) {
    // this method takes a list of paths where the indexes are numeric or empty and pads
    // each index with zeros upto a total width of 6. This means that the delete
    // feature during merge will work as long as there are not more than 999,999
    // elements in an array
    List<String> newPaths = new ArrayList<>(paths.size());

    for (String path : paths) {
      List<Token> tokens = Parser.getTokens(path);

      String s = "$";
      for (Token t : tokens) {
        if (t.isArray()) {
          ArrayToken at = (ArrayToken)t;
          s = s + "." + at.getField() + "[";

          ArrayToken.FilterType ft = at.getFilter().getType();
          if (ft == ArrayToken.FilterType.EMPTY) {
            s = s + "]";
          }
          else if (ft == ArrayToken.FilterType.INDEX) {
            // here we pad the value
            int index = at.getFilter().getIndex();
            String paddedIndex = String.format("%06d", index);
            s = s + paddedIndex + "]";
          }
          else if (ft == ArrayToken.FilterType.NAME_VALUE) {
            throw new RuntimeException("jdoc_err_67"+ path);
          }
        }
        else {
          s = s + "." + t.getField();
        }
      }

      newPaths.add(s);
    }

    return newPaths;
  }

    /**
     * Removes zero-padding from array indexes in paths.
     * @param paths the list of paths with zero-padded indexes
     * @return a list of paths with numeric indexes (unpadded)
     */
    public static List<String> getNoPaddedIndexes(List<String> paths) {
    // this method takes a list of paths where the indexes are padded with
    // zeroes. It removes the zeros and returns the list of paths
    // it preserves the order in which the paths are stored
    List<String> newPaths = new ArrayList<>(paths.size());

    for (String path : paths) {
      List<Token> tokens = Parser.getTokens(path);

      String s = "$";
      for (Token t : tokens) {
        if (t.isArray()) {
          ArrayToken at = (ArrayToken)t;
          s = s + "." + at.getField() + "[";

          ArrayToken.FilterType ft = at.getFilter().getType();
          if (ft == ArrayToken.FilterType.EMPTY) {
            s = s + "]";
          }
          else if (ft == ArrayToken.FilterType.INDEX) {
            // here we remove the leading zeros
            int index = at.getFilter().getIndex();
            s = s + index + "]";
          }
          else if (ft == ArrayToken.FilterType.NAME_VALUE) {
            throw new RuntimeException("jdoc_err_67"+ path);
          }
        }
        else {
          s = s + "." + t.getField();
        }
      }

      newPaths.add(s);
    }

    return newPaths;
  }

}
