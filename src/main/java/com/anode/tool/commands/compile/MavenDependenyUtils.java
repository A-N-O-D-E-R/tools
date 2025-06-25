package com.anode.tool.commands.compile;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;



@Slf4j
public class MavenDependenyUtils {

    public static Optional<URI> getParent(URI pom) {
        try {
            // Parse the POM file
            Document doc = parseXml(pom);
            
            // Look for <parent> element
            NodeList parentList = doc.getElementsByTagName("parent");
            if (parentList.getLength() > 0) {
                Element parentElement = (Element) parentList.item(0);
                String groupId = getElementValue(parentElement, "groupId");
                String artifactId = getElementValue(parentElement, "artifactId");
                String version = getElementValue(parentElement, "version");

                if (isValidParentData(groupId, artifactId, version)) {
                    return Optional.of(buildParentUri(pom, groupId, artifactId, version));
                }
            } else {
                log.error("<parent> element not found in the POM file.");
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle specific exception if necessary
            return Optional.empty();
        }
    }

    public static Optional<URI> getLastestSnapshotBuild(URI pom) {
        try{
            URI metadata =pom.resolve("./maven-metadata.xml");
            Document doc = parseXml(metadata);
            NodeList versionList = doc.getElementsByTagName("snapshotVersions");
            if (versionList.getLength() > 0) {
                String version = getElementValue((Element) versionList.item(0), "value");
                String[] paths = pom.resolve(".").toString().split("/");
                // we will suppose that the repo respect maven convention : https://Host/stuf/group/id/artifact/version/jar
                return Optional.of(URI.create(pom.toString().replace(paths[paths.length-1], version)));
            } else {
                log.error("<parent> element not found in the POM file.");
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle specific exception if necessary
            return Optional.empty();
        }
    }

    // Helper method to construct the POM path
    public static Path constructPath(Path repo,String groupId, String artefactId, String version, String extension) throws URISyntaxException {
        return Path.of(new URI(repo + "/"+groupId.replaceAll("\\.","/")+"/"+artefactId+"/" + version + "/"+artefactId+"-" + version + "."+ extension));
    }


    public static Optional<String> getLastestVersion(Path repo, String groupId, String artefactId) {
        try{
            URI metadata =constructMetaDataPath(repo,groupId,artefactId);
            Document doc = parseXml(metadata);
            NodeList versionList = doc.getElementsByTagName("versions");
            if (versionList.getLength() > 0) {
                String version = getElementValue((Element) versionList.item(0), "version");
                return Optional.of(version);
            } else {
                log.error("<parent> element not found in the POM file.");
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle specific exception if necessary
            return Optional.empty();
        }
    }

    private static URI constructMetaDataPath(Path repo, String groupId, String artefactId) throws URISyntaxException {
        return new URI(repo + "/"+groupId.replaceAll("\\.","/")+"/"+artefactId+"/maven-metadata.xml");
    }

    private static Document parseXml(URI pom) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(Files.readAllBytes(Path.of(pom)));
        Document doc = builder.parse(inputStream);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private static String getElementValue(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }

    private static boolean isValidParentData(String groupId, String artifactId, String version) {
        return Objects.nonNull(groupId) && Objects.nonNull(artifactId) && Objects.nonNull(version);
    }

    private static URI buildParentUri(URI pom, String groupId, String artifactId, String version) throws Exception {
        Path pomPath = Path.of(pom.getPath());
        // TODO: fix that shitty code, it should be deal be the https FileSystemProvider
        if(pom.toString().contains("@")){
            String baseUriPath = pom.getScheme()+"://"+pomPath.getName(0) + "/" + pomPath.getName(1) + "/"+pomPath.getName(2) +"/"
            + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version + "/"
            + artifactId + "-" + version + ".pom";
            return new URI(baseUriPath);
        }else{
            String baseUriPath = pomPath.getName(0) + "/" + pomPath.getName(1) + "/"
                + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version + "/"
                + artifactId + "-" + version + ".pom";
            return new URI(pom.getScheme(), pom.getHost(), baseUriPath);
        }
            
    }

    public static Path generateMavenTmpSettings(String[][] data) throws IOException{
        try{
            Path settings = Files.createTempFile("settings",".xml");
            generateMavenSettings(data,settings);
            return settings;
        }catch(ParserConfigurationException | TransformerException | IOException exception){
            throw new IOException("unable to generate settings file", exception);
        }
        
    }


    public static void generateMavenSettings(String[][] data, Path settingsPath) throws ParserConfigurationException, TransformerException, IOException{
        // Create a new DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        // Create a new DOM Document object
        Document document = builder.newDocument();
        
        // Create the root element <settings>
        Element settingsElement = document.createElement("settings");
        document.appendChild(settingsElement);

        // Create <servers> element and append to <settings>
        Element serversElement = document.createElement("servers");
        settingsElement.appendChild(serversElement);

        // Loop through server data and create <server> elements
        for (String[] server : data) {
            // Create <server> element
            Element serverElement = document.createElement("server");
            serversElement.appendChild(serverElement);

            // Create <id>, <username>, and <password> for each server
            Element idElement = document.createElement("id");
            idElement.appendChild(document.createTextNode(server[0])); // server id
            serverElement.appendChild(idElement);

            Element usernameElement = document.createElement("username");
            usernameElement.appendChild(document.createTextNode(server[1])); // username
            serverElement.appendChild(usernameElement);

            Element passwordElement = document.createElement("password");
            passwordElement.appendChild(document.createTextNode(server[2])); // password (token)
            serverElement.appendChild(passwordElement);
        }

        // Transform the DOM document to an XML string
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        try (BufferedWriter writer = Files.newBufferedWriter(settingsPath)) {
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            System.out.println("XML saved to " + settingsPath.toAbsolutePath());
        }
    }
}
