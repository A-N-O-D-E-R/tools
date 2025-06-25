package com.anode.tool.commands.compile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.anode.tool.PathUtils;
import com.anode.tool.commands.zip.Zip;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ClassPathUtils {
    
    public static List<String> findAnnotedClasses(Path jar, Set<Class> annotedClassType) throws IOException, ClassNotFoundException{
        Path tmpDir = Path.of(System.getProperty("java.io.tmpdir"));
        Path folder = tmpDir.resolve(jar.getFileName().toString().replace(".jar", ""));
        Path tmpJar = tmpDir.resolve(jar.getFileName().toString());
        PathUtils.copyPath(jar, tmpJar);
        Zip.unzip(tmpJar.toAbsolutePath().toString(), folder.toAbsolutePath().toString());
        ClassLoader cl = new CustomTempClassLoader(listClassFiles(folder).stream().collect(Collectors.toMap(path -> getClassName(path,folder), path->path.toAbsolutePath().toString())));
        List<Class<?>> classes = findAnnotedClasses(folder, folder,annotedClassType,cl);
        return classes.stream().map(clazz -> clazz.getName()).collect(Collectors.toList());
    }

    public static List<Class<?>> findAnnotedClasses(Path directory, Path rootDirectory, Set<Class> annotedClassType, ClassLoader classLoader) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new LinkedList<>();
        for (Path path : listClassFiles(directory)) {
            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                classes.addAll(findAnnotedClasses(path, rootDirectory,annotedClassType, classLoader));
            } else {
                String className =  getClassName(path, rootDirectory);
                Class<?> clazz = Class.forName(className, false, classLoader);
                if (checkIfClassHaveAtLeastOneAnnotation(clazz,annotedClassType)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

    private static String getClassName(Path classFilePath, Path rootDirectory) {
        Path relativePath = rootDirectory.relativize(classFilePath);
        String packageStructure = relativePath.toString().replace(Path.of("").toFile().separator, ".");
        String className = packageStructure.replace(".class", "");
        return className;
    }


    private static boolean checkIfClassHaveAtLeastOneAnnotation(Class clazz,Set<Class> annotedClassType){
        boolean result = false;
        for(Class annotation : annotedClassType){
            result = result || (clazz.getAnnotation(annotation) != null);
        }
        return result;
    }

    public static List<Path> listClassFiles(Path directory) throws IOException {
        List<Path> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    // Recursively list class files in the subdirectory
                    result.addAll(listClassFiles(entry));
                } else if (entry.toString().endsWith(".class")) {
                    // Add the class file to the result
                    result.add(entry);
                }
            }
        }
        return result;
    }


    public static Object instanciate(String className, ClassLoader classLoader, Object[] parameters, Class[] parameterTypes) throws java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.lang.reflect.InvocationTargetException,java.lang.InstantiationException{
        Class connecteurClass = Class.forName(className, false, classLoader);
        java.lang.reflect.Constructor constructor = findMatchingConstructor(connecteurClass, parameterTypes);
        return constructor.newInstance(parameters);
    }
    
    private static java.lang.reflect.Constructor findMatchingConstructor(Class connectorClass, Class[] parametersTypes) {
        int parameterTypesLength = parametersTypes.length;
        Class composantReseauUsageClass = parametersTypes[0] ;

        constructorLoop: for ( java.lang.reflect.Constructor constructor : connectorClass.getConstructors()) {
            Class[] constructorParameterTypes = constructor.getParameterTypes();

            if (constructorParameterTypes.length != parameterTypesLength) {
                continue constructorLoop;
            }

            if (!constructorParameterTypes[0].isAssignableFrom(composantReseauUsageClass)) {
                continue constructorLoop;
            }

            for (int i = 1; i < parameterTypesLength; i++) {
                if (!constructorParameterTypes[i].equals(parametersTypes[i])) {
                    continue constructorLoop;
                }
            }

            return constructor;
        }

        String parametersAsString = "" ;

        for(Class parameterType : parametersTypes) {
            parametersAsString+=parameterType.getName()+" " ;
        }

        throw new RuntimeException("unable to find a matching constuctor for " + connectorClass.getName()+" with parameters types "+parametersAsString);
    }
}
