package com.anode.tool.document;

/**
 * Enumeration of validation types for documents.
 */
public enum validationTypes {
    /**
     * Validate all data paths.
     */
    ALL_DATA_PATHS,

    /**
     * Validate only model-defined paths.
     */
    ONLY_MODEL_PATHS,

    /**
     * Validate only at read and write operations.
     */
    ONLY_AT_READ_WRITE
}
