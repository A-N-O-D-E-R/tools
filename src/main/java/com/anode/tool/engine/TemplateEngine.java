package com.anode.tool.engine;

import com.hubspot.jinjava.Jinjava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * Template engine for rendering Jinja2 templates with property substitution.
 */
public class TemplateEngine {

    /**
     * Properties to use as variables in template rendering.
     */
    private Map<String, Object> properties;

    /**
     * The Jinja template processor.
     */
    private Jinjava templateProcessor;

    /**
     * Constructs a TemplateEngine with the given properties.
     * @param prop the properties to use for template rendering
     */
    public TemplateEngine(Properties prop) {
        templateProcessor = new Jinjava();
        this.properties = loopConvert(prop);
    }

    /**
     * Constructs a TemplateEngine by loading properties from a file.
     * @param propPath the path to the properties file
     */
    public TemplateEngine(Path propPath) {
        templateProcessor = new Jinjava();
        this.properties = loopConvert(loadProperty(propPath));
    }

    /**
     * Factory method to create a TemplateEngine from a properties file.
     * @param propPath the path to the properties file
     * @return a new TemplateEngine instance
     */
    public static TemplateEngine create(Path propPath) {
        return new TemplateEngine(propPath);
    }

    /**
     * Renders a template file using the configured properties.
     * @param template the path to the template file
     * @return the rendered template as a string
     * @throws IOException if the template file cannot be read
     */
    public String render(Path template) throws IOException {
        String rawTemplateString = new String(Files.readAllBytes(template), StandardCharsets.UTF_8);
        return templateProcessor.render(rawTemplateString, this.properties);
    }

    /**
     * Renders a template file with Unix-style variable syntax converted to Jinja syntax.
     * @param template the path to the template file
     * @return the rendered template as a string
     * @throws IOException if the template file cannot be read
     */
    public String renderUnixVar(Path template) throws IOException {
        String rawTemplateString = unixToEngineVariable( new String(Files.readAllBytes(template), StandardCharsets.UTF_8));
        return templateProcessor.render(rawTemplateString, this.properties);
    }

    /**
     * Renders a template string directly without reading from a file.
     * @param templateContent the template content to render
     * @return the rendered template as a string
     */
    public String rawStringRender(String templateContent) {
        return templateProcessor.render(templateContent, this.properties);
    }

    /**
     * Renders a template with additional fragments.
     * @param template the path to the main template file
     * @param fragmentsPath the list of fragment file paths to include
     * @return the rendered template as a string
     * @throws IOException if any template or fragment file cannot be read
     */
    public String render(Path template, List<Path> fragmentsPath) throws IOException {
        StringBuilder rawTemplateString = new StringBuilder();
        rawTemplateString
                .append(new String(Files.readAllBytes(template), StandardCharsets.UTF_8))
                .append('\n');
        for (Path fragment : fragmentsPath) {
            try {
                rawTemplateString
                        .append(new String(Files.readAllBytes(fragment), StandardCharsets.UTF_8))
                        .append('\n');
            } catch (IOException ioException) {
                throw ioException;
            }
        }
        return templateProcessor.render(rawTemplateString.toString(), this.properties);
    }

    /**
     * Converts flat properties (with dot notation keys) into a nested map structure.
     * For example: "tool.apache.jar=value" becomes {tool: {apache: {jar: "value"}}}.
     * @param prop the properties to convert
     * @return a nested map structure representing the properties
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> loopConvert(Properties prop) {
        HashMap<String, Object> retMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : prop.entrySet()) {
            Map<String, Object> tmpMap = retMap;
            if (!String.valueOf(entry.getValue()).isEmpty()) {
                List<String> keyList =
                        List.of(String.valueOf(entry.getKey()).split("\\.")).stream()
                                .map(key -> key.replace("-", "_"))
                                .collect(Collectors.toList());
                for (String key : keyList) {
                    if (keyList.indexOf(key) == (keyList.size() - 1)) {
                        tmpMap.put(
                                keyList.get(keyList.size() - 1), String.valueOf(entry.getValue()));
                    } else {
                        tmpMap.computeIfAbsent(key, k -> new HashMap<String, Object>());
                        tmpMap = (Map<String, Object>) tmpMap.get(key);
                    }
                }
            }
        }
        return retMap;
    }

    /**
     * Loads properties from a file.
     * @param path the path to the properties file
     * @return the loaded properties
     * @throws RuntimeException if the file cannot be read
     */
    public static Properties loadProperty(Path path) {
        try (InputStream input = new ByteArrayInputStream(Files.readAllBytes(path))) {
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            log.error("error while reading file" + path.toString(), ex);
            throw new RuntimeException("error while reading file" + path.toString());
        }
    }

    /**
     * Converts Unix-style variables (${var}) to Jinja template syntax ({{var}}).
     * @param in the input string with Unix-style variables
     * @return the converted string with Jinja-style variables
     */
    private String unixToEngineVariable(String in) {
        return in
          .replaceAll("(\\$\\{([^\\s]*)\\})", "{{$2}}")
          .replaceAll("-(?=[^\\{\\{\\}\\}]*\\}\\})", "_");
      }

}
