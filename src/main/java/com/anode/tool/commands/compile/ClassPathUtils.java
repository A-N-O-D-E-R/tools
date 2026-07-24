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


/**
 * Utility class for classpath operations.
 */
@Slf4j
public class ClassPathUtils {

	/**
	 * Finds annotated classes in a JAR file.
	 * @param jar the JAR file path
	 * @param annotedClassType the annotation types to look for
	 * @return list of class names that have the specified annotations
	 * @throws IOException if an I/O error occurs
	 * @throws ClassNotFoundException if a class is not found
	 */
	public static List<String> findAnnotedClasses(Path jar, Set<Class> annotedClassType) throws IOException, ClassNotFoundException {
        Path tmpDir = Path.of(System.getProperty("java.io.tmpdir"));
        Path folder = tmpDir.resolve(jar.getFileName().toString().replace(".jar", ""));
        Path tmpJar = tmpDir.resolve(jar.getFileName().toString());
        PathUtils.copyPath(jar, tmpJar);
        Zip.unzip(tmpJar.toAbsolutePath().toString(), folder.toAbsolutePath().toString());
        ClassLoader cl = new CustomTempClassLoader(listClassFiles(folder).stream().collect(Collectors.toMap(path -> getClassName(path,folder), path->path.toAbsolutePath().toString())));
        List<Class<?>> classes = findAnnotedClasses(folder, folder,annotedClassType,cl);
        return classes.stream().map(clazz -> clazz.getName()).collect(Collectors.toList());
    }

	/**
	 * Recursively finds annotated classes in a directory.
	 * @param directory the directory to search
	 * @param rootDirectory the root directory for class name resolution
	 * @param annotedClassType the annotation types to look for
	 * @param classLoader the class loader to use
	 * @return list of classes that have the specified annotations
	 * @throws ClassNotFoundException if a class is not found
	 * @throws IOException if an I/O error occurs
	 */
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

	/**
	 * Gets the class name from a class file path.
	 * @param classFilePath the path to the class file
	 * @param rootDirectory the root directory for class name resolution
	 * @return the fully qualified class name
	 */
	private static String getClassName(Path classFilePath, Path rootDirectory) {
        Path relativePath = rootDirectory.relativize(classFilePath);
        String packageStructure = relativePath.toString().replace(Path.of("").toFile().separator, ".");
        String className = packageStructure.replace(".class", "");
        return className;
    }


    /**
     * Checks if a class has at least one of the specified annotations.
     * @param clazz the class to check
     * @param annotedClassType the set of annotation classes to look for
     * @return true if the class has at least one of the specified annotations
     */
    private static boolean checkIfClassHaveAtLeastOneAnnotation(Class clazz, Set<Class> annotedClassType) {
        boolean result = false;
        for (Class annotation : annotedClassType) {
            result = result || (clazz.getAnnotation(annotation) != null);
        }
        return result;
    }

	/**
	 * Lists all class files in a directory recursively.
	 * @param directory the directory to search
	 * @return list of paths to class files
	 * @throws IOException if an I/O error occurs
	 */
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


	/**
	 * Instantiates a class with the given parameters.
	 * @param className the fully qualified class name
	 * @param classLoader the class loader to use
	 * @param parameters the constructor parameters
	 * @param parameterTypes the constructor parameter types
	 * @return an instance of the class
	 * @throws ClassNotFoundException if the class is not found
	 * @throws IllegalAccessException if access to the constructor is denied
	 * @throws java.lang.reflect.InvocationTargetException if the constructor throws an exception
	 * @throws java.lang.InstantiationException if the class cannot be instantiated
	 */
	public static Object instanciate(String className, ClassLoader classLoader, Object[] parameters, Class[] parameterTypes) throws java.lang.ClassNotFoundException,java.lang.IllegalAccessException,java.lang.reflect.InvocationTargetException,java.lang.InstantiationException {
        Class connecteurClass = Class.forName(className, false, classLoader);
        java.lang.reflect.Constructor constructor = findMatchingConstructor(connecteurClass, parameterTypes);
        return constructor.newInstance(parameters);
    }
    
    /**
     * Finds a constructor matching the given parameter types.
     * @param connectorClass the class to find a constructor for
     * @param parametersTypes the parameter types to match
     * @return the matching constructor
     * @throws RuntimeException if no matching constructor is found
     */
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
