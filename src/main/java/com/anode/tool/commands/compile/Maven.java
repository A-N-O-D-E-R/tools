package com.anode.tool.commands.compile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.cli.MavenCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Maven {
   
    private PrintStream  stdout;
    private PrintStream  stderr;
    private MavenCli cli;
    private String[][] settingData;

    public Maven(){
        cli = new MavenCli();
        stdout=System.out;
        stderr=System.err;
    }

    public Maven(String[][] settingData){
        cli = new MavenCli();
        stdout=System.out;
        stderr=System.err;
        this.settingData=settingData;
    }

    public static Maven create(PrintStream stdout, PrintStream stderr, String[][] settingData){
        return new Maven(stdout,stderr,settingData);
    }

    public Maven(PrintStream stdout, PrintStream stderr){
        cli = new MavenCli();
        this.stdout=stdout;
        this.stderr=stderr;
    }

    public Maven(PrintStream stdout, PrintStream stderr, String[][] settingData){
        cli = new MavenCli();
        this.stdout=stdout;
        this.stderr=stderr;
        this.settingData=settingData;
    }
    
    
        // Assuming cli and stdout/stderr are defined elsewhere in your class
        private int run(Path pom, String... args) {
            String salt = UUID.randomUUID().toString();
            try {
                // Create the necessary directories for the new path
                Path targetDir = pom.getParent().resolve(salt);
                Files.createDirectories(targetDir);
    
                // Copy the pom.xml to the new location with the salt folder
                Files.copy(pom, targetDir.resolve("pom.xml"), StandardCopyOption.REPLACE_EXISTING);
                if (settingData != null) {
                    // Generate Maven temporary settings
                    Path settings = targetDir.resolve("settings.xml");
                    MavenDependenyUtils.generateMavenSettings(settingData, settings);
                    // Create a new array with additional settings
                    args = Arrays.copyOf(args, args.length + 2);  // Add space for the new arguments
                    args[args.length - 2] = "-s"; // Set the -s option
                    args[args.length - 1] = settings.toAbsolutePath().toString(); // Set the settings path
                }
            } catch (Exception e) {
                // Log the exception for better understanding
                e.printStackTrace();
                return -1; // Return a failure code or handle as needed
            }
    
            // Set the system property for the Maven project directory
            System.setProperty("maven.multiModuleProjectDirectory", pom.getParent().resolve(salt).toString());
    
            // Assuming cli.doMain() returns an int for the exit code
            return cli.doMain(args, pom.getParent().resolve(salt).toString(), stdout, stderr);
        }

    

    public void installSingle(Path pom){
        Path tmpDir = Path.of(System.getProperty("java.io.tmpdir"));
        patchInssue(tmpDir);
        log.info(pom.getFileName().toString());
        Path tempFile = tmpDir.resolve(pom.getFileName().toString());
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tempFile, StandardOpenOption.CREATE);
                    PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
                String data = new String(Files.readAllBytes(pom), StandardCharsets.UTF_8);
                printWriter.println(data);
                run(tempFile,"clean", "install", "-N");
        } catch (IOException e) {
                e.printStackTrace();
                log.error("error while creating remote configuration", tempFile);
        }finally{
            try {
                Files.delete(tempFile);
            } catch (IOException e) {
                log.error("unable to delete temp file", e);
            }
        }
       
        
    }

    public void patchInssue(Path folder){
        //this is an horible hack but the parent of controler is broken
        try {
            Files.createDirectories(folder.resolve("lib/extern"));
            Files.createDirectories(folder.resolve("config/commune"));
            Files.createDirectories(folder.resolve("config/production"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void copyDependenciesIntoFolder(Path pom, Path outputFolder) {
        Path tmpDir = Path.of(System.getProperty("java.io.tmpdir"));
        Path tempFile = tmpDir.resolve(pom.getFileName().toString());
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tempFile, StandardOpenOption.CREATE);
                    PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
                String data = new String(Files.readAllBytes(pom), StandardCharsets.UTF_8);
                printWriter.println(data);
                run(tempFile,"dependency:copy-dependencies", "-DoutputDirectory="+outputFolder.toString());
        } catch (IOException e) {
                log.error("error while creating remote configuration", e);
        }finally{
            try {
                Files.delete(tempFile);
            } catch (IOException e) {
                log.error("unable to delete temp file", e);
            }
        }
    }


    public Set<String> getClassPath(Path pom) throws IOException{
        Path tmpDir = Path.of(System.getProperty("java.io.tmpdir"));
        Path tempPom = tmpDir.resolve(pom.getFileName().toString());
        Path tempFile = tmpDir.resolve(pom.getFileName().toString()+".classpath");
        Optional<URI> parent = MavenDependenyUtils.getParent(pom.toUri());
        if(parent.isPresent()){
            // need the parent to resolve sister dependencies
            installSingle(Path.of(parent.get()));
        }
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tempPom, StandardOpenOption.CREATE);
                    PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
                String data = new String(Files.readAllBytes(pom), StandardCharsets.UTF_8);
                printWriter.write(data);
                run(tempPom, "dependency:build-classpath", "-Dmdep.outputFile="+tempFile.toString());
        } catch (IOException e) {
                log.error("error while creating remote configuration", e);
        }finally{
            
        }
        return Files.lines(tempFile).flatMap(line -> Stream.of(line.split(":"))).collect(Collectors.toSet());
    }
}
