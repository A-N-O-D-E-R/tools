package com.anode.tool.document;

/**
 * Enumeration representing the difference result when comparing paths in documents.
 */
public enum PathDiffResult {
    /**
     * Paths are equal.
     */
    EQUAL,

    /**
     * Paths exist in both documents but have different values.
     */
    DIFFERENT,

    /**
     * Path exists only in the left document.
     */
    ONLY_IN_LEFT,

    /**
     * Path exists only in the right document.
     */
    ONLY_IN_RIGHT
}
