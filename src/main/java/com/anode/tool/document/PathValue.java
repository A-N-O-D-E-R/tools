package com.anode.tool.document;

/**
 * Represents a value at a specific path in a document with its data type.
 */
public class PathValue {

    /**
     * The path to the value.
     */
    private String path;

    /**
     * The value at the path.
     */
    private Object value;

    /**
     * The data type of the value.
     */
    private DataType dataType;

    /**
     * Constructs a PathValue with the specified path, value, and data type.
     * @param path the path to the value
     * @param value the value at the path
     * @param dataType the data type of the value
     */
    public PathValue(String path, Object value, DataType dataType) {
        this.path = path;
        this.value = value;
        this.dataType = dataType;
    }

    /**
     * Gets the path to the value.
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the value at the path.
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Gets the data type of the value.
     * @return the data type
     */
    public DataType getDataType() {
        return dataType;
    }

}
