package com.anode.tool.document;

/**
 * Represents a single token in a path expression.
 */
class Token {

    /**
     * The field name represented by this token.
     */
    private String field;

    /**
     * Whether this token is a leaf node in the path.
     */
    private boolean isLeaf;

    /**
     * Constructs a Token with the specified field name and leaf status.
     * @param field the field name
     * @param isLeaf whether this is a leaf token
     */
    public Token(String field, boolean isLeaf) {
        this.field = field;
        this.isLeaf = isLeaf;
    }

    /**
     * Gets the field name represented by this token.
     * @return the field name
     */
    public String getField() {
        return field;
    }

    /**
     * Checks if this token represents an array element.
     * @return false (overridden in subclasses)
     */
    public boolean isArray() {
        return false;
    }

    /**
     * Checks if this token is a leaf node.
     * @return true if this is a leaf token
     */
    public boolean isLeaf() {
        return isLeaf;
    }

}
