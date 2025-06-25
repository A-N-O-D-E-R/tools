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
public class TemplateEngine {

    private Map<String, Object> properties;
    private Jinjava templateProcessor;

    public TemplateEngine(Properties prop) {
        templateProcessor = new Jinjava();
        this.properties = loopConvert(prop);
    }

    public TemplateEngine(Path propPath) {
        templateProcessor = new Jinjava();
        this.properties = loopConvert(loadProperty(propPath));
    }

    public static TemplateEngine create(Path propPath) {
        return new TemplateEngine(propPath);
    }

    public String render(Path template) throws IOException {
        String rawTemplateString = new String(Files.readAllBytes(template), StandardCharsets.UTF_8);
        return templateProcessor.render(rawTemplateString, this.properties);
    }

    public String renderUnixVar(Path template) throws IOException {
        String rawTemplateString = unixToEngineVariable( new String(Files.readAllBytes(template), StandardCharsets.UTF_8));
        return templateProcessor.render(rawTemplateString, this.properties);
    }

    
    public String rawStringRender(String templateContent) {
        return templateProcessor.render(templateContent, this.properties);
    }

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

    /*  Convert property in DeepMap of value
     * For example :
     *      tool.apache.jar= tool.jar will become
     * MAP = {
     *   tool:{
     *       apache: {
     *               jar: "tool.jar"
     *               }
     *        }
     * }
     *
     * BAD Implem
     *
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

    private String unixToEngineVariable(String in) {
        return in
          .replaceAll("(\\$\\{([^\\s]*)\\})", "{{$2}}")
          .replaceAll("-(?=[^\\{\\{\\}\\}]*\\}\\})", "_");
      }

}
