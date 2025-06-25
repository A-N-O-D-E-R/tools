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

@Slf4j

public class CustomTempClassLoader  extends ClassLoader{

        private Map<String, String> classPathMap = new HashMap<>();
    
        public CustomTempClassLoader(Map<String, String> classPathMap) {
            this.classPathMap=classPathMap;
        }
    
        // The findClass method is overridden
        @Override
        public Class<? > findClass(String name) throws ClassNotFoundException {
            String classPath = classPathMap.get(name);
            File file = new File(classPath);
            if (! file.exists()) {
                throw new ClassNotFoundException();
            }
            byte[] classBytes = getClassData(file);
            if (classBytes == null || classBytes.length == 0) {
                throw new ClassNotFoundException();
            }
            return defineClass(classBytes, 0, classBytes.length);
        }
    
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
