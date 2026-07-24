package com.anode.tool.document;

/**
 * Enumeration of data types supported in documents.
 */
public enum DataType {
    /**
     * String type.
     */
    STRING("string"),

    /**
     * Date type.
     */
    DATE("date"),

    /**
     * Boolean type.
     */
    BOOLEAN("boolean"),

    /**
     * Integer type.
     */
    INTEGER("integer"),

    /**
     * Long type.
     */
    LONG("long"),

    /**
     * Decimal/numeric type.
     */
    DECIMAL("decimal");

    /**
     * The string representation of the data type.
     */
    private String dataType;

    /**
     * Constructs a DataType with the specified string representation.
     * @param dataType the string representation
     */
    DataType(String dataType) {
      this.dataType = dataType;
    }

    /**
     * Returns the string representation of this data type.
     * @return the string representation
     */
    public String toString() {
      return dataType;
    }

}
