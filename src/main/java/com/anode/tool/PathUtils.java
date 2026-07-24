package com.anode.tool;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Utility class for path and file operations.
 */
public class PathUtils {

    /**
     * Copies a file from one path to another.
     * @param from the source file path
     * @param to the destination file path
     * @throws IOException if an I/O error occurs during the copy
     */
    public static void copyPath(Path from, Path to) throws IOException{
        try (InputStream in =new ByteArrayInputStream(Files.readAllBytes(from));
             OutputStream out = Files.newOutputStream(to, StandardOpenOption.CREATE)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Copies multiple files to a destination directory.
     * @param paths the list of source file paths to copy
     * @param to the destination directory path
     * @throws IOException if any file cannot be copied
     */
    public static void copyAllTo(List<Path> paths, Path to) throws IOException{
        List<String> errors = new ArrayList<>();
        for(Path deps : paths){
            try {
                PathUtils.copyPath(deps, to.resolve(deps.getFileName().toString()));
            } catch (IOException e) {
                e.printStackTrace();
                errors.add(deps.toString());
            }
        }
        if(errors.size()>0){
            throw new IOException("unable to fetch the following depps "+errors);
        }
    }


     /**
      * Loads properties from a file.
      * @param propertiesFile the path to the properties file
      * @return the loaded properties
      * @throws IOException if the file cannot be read
      */
     public static Properties loadProperties(Path propertiesFile) throws IOException {
        try (InputStream input = new ByteArrayInputStream(Files.readAllBytes(propertiesFile))) {
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        }
    }

    /**
     * Recursively deletes a file or directory.
     * @param path the path to delete
     * @throws IOException if the deletion fails
     */
    public static void deleteRecursively(Path path) throws IOException{
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    deleteRecursively(entry);
                }
            }
        }
        Files.delete(path);
    }
}
