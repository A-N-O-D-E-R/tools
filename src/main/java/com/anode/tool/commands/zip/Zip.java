package com.anode.tool.commands.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Zip {

  /**
   * A constants for buffer size used to read/write data
   */
  private static final int BUFFER_SIZE = 4096;

  /**
   * Compresses a list of files to a destination zip file
   * @param listFiles A collection of files and directories
   * @param destZipFile The path of the destination zip file
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void zip(List<File> listFiles, String destZipFile)
    throws FileNotFoundException, IOException {
    ZipOutputStream zos = new ZipOutputStream(
      new FileOutputStream(destZipFile)
    );
    for (File file : listFiles) {
      if (file.isDirectory()) {
        zipDirectory(file, file.getName(), zos);
      } else {
        zipFile(file, zos);
      }
    }
    zos.flush();
    zos.close();
  }

  /**
   * Compresses files represented in an array of paths
   * @param files a String array containing file paths
   * @param destZipFile The path of the destination zip file
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void zip(String[] files, String destZipFile)
    throws FileNotFoundException, IOException {
    List<File> listFiles = new ArrayList<File>();
    for (int i = 0; i < files.length; i++) {
      listFiles.add(new File(files[i]));
    }
    zip(listFiles, destZipFile);
  }

  /**
   * Adds a directory to the current zip output stream
   * @param folder the directory to be  added
   * @param parentFolder the path of parent directory
   * @param zos the current zip output stream
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static void zipDirectory(
    File folder,
    String parentFolder,
    ZipOutputStream zos
  ) throws FileNotFoundException, IOException {
    for (File file : folder.listFiles()) {
      if (file.isDirectory()) {
        zipDirectory(file, parentFolder + "/" + file.getName(), zos);
        continue;
      }
      zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
      BufferedInputStream bis = new BufferedInputStream(
        new FileInputStream(file)
      );
      long bytesRead = 0;
      byte[] bytesIn = new byte[BUFFER_SIZE];
      int read = 0;
      while ((read = bis.read(bytesIn)) != -1) {
        zos.write(bytesIn, 0, read);
        bytesRead += read;
      }
      zos.closeEntry();
    }
  }

  /**
   * Adds a file to the current zip output stream
   * @param file the file to be added
   * @param zos the current zip output stream
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static void zipFile(File file, ZipOutputStream zos)
    throws FileNotFoundException, IOException {
    zos.putNextEntry(new ZipEntry(file.getName()));
    BufferedInputStream bis = new BufferedInputStream(
      new FileInputStream(file)
    );
    long bytesRead = 0;
    byte[] bytesIn = new byte[BUFFER_SIZE];
    int read = 0;
    while ((read = bis.read(bytesIn)) != -1) {
      zos.write(bytesIn, 0, read);
      bytesRead += read;
    }
    zos.closeEntry();
  }

  public static void unzip(String zipFilePath, String destDir) {
    File dir = new File(destDir);
    // create output directory if it doesn't exist
    if (!dir.exists()) dir.mkdirs();
    FileInputStream fis;
    //buffer for read and write data to file
    byte[] buffer = new byte[1024];
    try {
      fis = new FileInputStream(zipFilePath);
      ZipInputStream zis = new ZipInputStream(fis);
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        String fileName = zipEntry.getName();
        File newFile = new File(destDir + File.separator + fileName);
        log.info("Unzipping to " + newFile.getAbsolutePath());
        //create directories for sub directories in zip
        new File(newFile.getParent()).mkdirs();
        if (zipEntry.isDirectory()) {
          if (!newFile.isDirectory() && !newFile.mkdirs()) {
            throw new IOException("Failed to create directory " + newFile);
          }
        } else {
          FileOutputStream fos = new FileOutputStream(newFile);
          int len;
          while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
          }
          fos.close();
        }
        //close this ZipEntry
        zis.closeEntry();
        zipEntry = zis.getNextEntry();
      }
      //close last ZipEntry
      zis.closeEntry();
      zis.close();
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnzipException("error while unziping file "+ zipFilePath, e);
    }
  }
}
