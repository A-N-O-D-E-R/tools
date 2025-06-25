package com.anode.tool.document;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import org.w3c.dom.Node;

public class XDocument implements Document {

     private static Map<String, Document> docModels = new ConcurrentHashMap<>();
  
    // for each model document, store a map of the constraint string and the corresponding JsonNode
    private static Map<String, JsonNode> docModelPaths = new ConcurrentHashMap<>();
    
    // for each regular expression pattern, store the compiled pattern
    private static Map<String, Pattern> compiledPatterns = new ConcurrentHashMap<>();
  
    private static validationTypes defaultValidationType = validationTypes.ALL_DATA_PATHS;

    // Type of the document (XML type could be different depending on your usage)
    private String type = "";

    private validationTypes validationType = validationTypes.ALL_DATA_PATHS;

    private boolean isValidated = false;

    protected JsonNode rootNode = null;

    // Root XML node of the document
    protected static final XmlMapper xmlMapper = (XmlMapper) new XmlMapper().configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

    private static final String NEW_LINE = System.getProperty("line.separator");

    public static void init() {
        init(validationTypes.ALL_DATA_PATHS);
    }

    public static void init(validationTypes validationType) {
        // should be done once at the start
        XDocument.defaultValidationType = validationType;
    }

    public static validationTypes getDefaultValidationType() {
        return defaultValidationType;
    }

    public XDocument() {
        try {
            rootNode = xmlMapper.readTree("<root></root>");
        } catch (IOException ex) {
            throw new RuntimeException("jdoc_err_1" + ex);
        }
    }


    public XDocument(String xml) {
        try {
            rootNode = xmlMapper.readTree(xml);
        } catch (IOException ex) {
            throw new RuntimeException("jdoc_err_1" + ex);
        }
    }

    public XDocument(String type, String xml) {
        init(type, xml, defaultValidationType);
    }

    public XDocument(String type, String xml, validationTypes validationType) {
        init(type, xml, validationType);
    }

    private void init(String type, String xml, validationTypes validationType) {
        if ((type == null) || (type.isEmpty())) {
            throw new RuntimeException("jdoc_err_56");
        }

        this.validationType = validationType;

        try {
            this.type = type;
            if (xml == null) {
                rootNode = xmlMapper.readTree("<root></root>");
            } else {
                rootNode = xmlMapper.readTree(xml);
            }

            if (validationType != validationTypes.ONLY_AT_READ_WRITE) {
                validate(type, validationType);
            }
        } catch (IOException ex) {
            throw new RuntimeException("jdoc_err_1" + ex);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        setType(type, defaultValidationType);
    }


    @Override
    public boolean isTyped() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTyped'");
    }


    @Override
    public DataType getLeafNodeDataType(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLeafNodeDataType'");
    }

    @Override
    public DataType getArrayValueLeafNodeDataType(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArrayValueLeafNodeDataType'");
    }


    @Override
    public void setType(String type, boolean validateAtReadWriteOnly) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setType'");
    }

    @Override
    public void setType(String type, validationTypes validationType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setType'");
    }

    @Override
    public void empty() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'empty'");
    }

    @Override
    public void deletePaths(List<String> pathsToDelete) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePaths'");
    }

    @Override
    public void deletePath(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePath'");
    }

    @Override
    public int getArraySize(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArraySize'");
    }

    @Override
    public int getArrayIndex(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArrayIndex'");
    }

    @Override
    public Boolean getBoolean(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBoolean'");
    }

    @Override
    public Integer getInteger(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInteger'");
    }

    @Override
    public String getString(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getString'");
    }

    @Override
    public Long getLong(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLong'");
    }

    @Override
    public BigDecimal getBigDecimal(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Override
    public Object getValue(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValue'");
    }

    @Override
    public Object getArrayValue(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArrayValue'");
    }

    @Override
    public Boolean getArrayValueBoolean(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArrayValueBoolean'");
    }

    @Override
    public Integer getArrayValueInteger(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArrayValueInteger'");
    }

    @Override
    public String getArrayValueString(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArrayValueString'");
    }

    @Override
    public Long getArrayValueLong(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArrayValueLong'");
    }

    @Override
    public BigDecimal getArrayValueBigDecimal(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArrayValueBigDecimal'");
    }

    @Override
    public String getJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getJson'");
    }

    @Override
    public String getPrettyPrintJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPrettyPrintJson'");
    }

    @Override
    public boolean pathExists(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pathExists'");
    }

    @Override
    public boolean isArray(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isArray'");
    }

    @Override
    public Document getDocument(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDocument'");
    }

    @Override
    public Document getContent(String path, boolean returnTypedDocument, boolean includeFullPath, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContent'");
    }

    @Override
    public void setContent(Document fromDoc, String fromPath, String toPath, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setContent'");
    }

    @Override
    public void setBoolean(String path, boolean value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBoolean'");
    }

    @Override
    public void setInteger(String path, int value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setInteger'");
    }

    @Override
    public void setLong(String path, long value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLong'");
    }

    @Override
    public void setBigDecimal(String path, BigDecimal value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBigDecimal'");
    }

    @Override
    public void setString(String path, String value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setString'");
    }

    @Override
    public void setArrayValueBoolean(String path, boolean value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setArrayValueBoolean'");
    }

    @Override
    public void setArrayValueInteger(String path, int value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setArrayValueInteger'");
    }

    @Override
    public void setArrayValueLong(String path, long value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setArrayValueLong'");
    }

    @Override
    public void setArrayValueBigDecimal(String path, BigDecimal value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setArrayValueBigDecimal'");
    }

    @Override
    public void setArrayValueString(String path, String value, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setArrayValueString'");
    }

    @Override
    public Document deepCopy() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deepCopy'");
    }

    @Override
    public void merge(Document d, List<String> pathsToDelete) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'merge'");
    }

    @Override
    public List<String> flatten() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flatten'");
    }

    @Override
    public List<PathValue> flattenWithValues() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'flattenWithValues'");
    }

    @Override
    public List<DiffInfo> getDifferences(Document right, boolean onlyDifferences) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDifferences'");
    }

    @Override
    public List<DiffInfo> getDifferences(String leftPath, Document right, String rightPath, boolean onlyDifferences) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDifferences'");
    }

   @Override
    @Deprecated
    public final void validate(String type) {
        // Validate the contents of the document for XML
        validate(type, validationTypes.ALL_DATA_PATHS);
    }

    private final void validate(String type, validationTypes validationType) {
        // Fetch the model for the given document type
        Document md = docModels.get(type);
        if (md == null) {
            throw new RuntimeException("Document model not found for " + type);
        }
        
        // Assuming rootNode is an XmlNode, you'll now validate with XML root nodes
        List<String> errorList = validate(((XDocument) md).rootNode, rootNode, "$.", type, validationType);
        
        // Process validation errors
        processErrors(errorList);
        
        // If the document is typed, mark as validated
        if (isTyped()) {
            isValidated = true;
        }
    }

    // Method to validate nodes for XML (you would need to implement XML-specific logic here)
    private List<String> validate(JsonNode  modelNode, JsonNode  documentNode, String path, String type, validationTypes validationType) {
        List<String> errors = new ArrayList<>();
        // Implement XML-specific validation here (e.g., compare XML nodes, use XPath, etc.)
        // This will depend on your validation logic, such as validating the structure, types, etc.
        return errors;
    }


    
    private void processErrors(List<String> errorList) {
        if (errorList.size() > 0) {
          StringBuffer sb = new StringBuffer();
          errorList.stream().forEach(s -> {
            s = s + NEW_LINE;
        sb.append(s);
      });
      throw new RuntimeException("jdoc_err_28"+sb.toString());
    }
  }
  
    @Override
    public void validateAllPaths(String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateAllPaths'");
    }

    @Override
    public void validateModelPaths(String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateModelPaths'");
    }

    @Override
    public boolean isLeafNode(String path, String... vargs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isLeafNode'");
    }

    @Override
    public validationTypes getValidationType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValidationType'");
    }
    
}
