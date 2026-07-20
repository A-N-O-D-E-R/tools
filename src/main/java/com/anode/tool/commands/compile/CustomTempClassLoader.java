package com.anode.tool.commands.compile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom class loader for temporary classes.
 */
@Slf4j
public class CustomTempClassLoader extends ClassLoader {

	private Map<String, String> classPathMap = new HashMap<>();

	/**
	 * Constructs a CustomTempClassLoader with the given class path map.
	 * @param classPathMap mapping of class names to file paths
	 */
	public CustomTempClassLoader(Map<String, String> classPathMap) {
		this.classPathMap = classPathMap;
	}

	/**
	 * Finds and loads a class by name.
	 * @param name the fully qualified class name
	 * @return the loaded class
	 * @throws ClassNotFoundException if the class is not found
	 */
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
            String classPath = classPathMap.get(name);
            File file = new File(classPath);
            if (! file.exists()) {
                throw new ClassNotFoundException();
            }
            byte[] classBytes = getClassData(file);
            if (classBytes == null || classBytes.length == 0) {
                throw new ClassNotFoundException();
            }
			return defineClass(name, classBytes, 0, classBytes.length);
		}

		/**
		 * Gets the class data from a file.
		 * @param file the class file
		 * @return the class bytes
		 */
		private byte[] getClassData(File file) {
            try (InputStream ins = new FileInputStream(file); ByteArrayOutputStream baos = new
                    ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesNumRead = 0;
                while ((bytesNumRead = ins.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesNumRead);
                }
                return baos.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new byte[] {};
        }

    }
