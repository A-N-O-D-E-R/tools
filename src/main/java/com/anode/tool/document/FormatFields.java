package com.anode.tool.document;

/**
 * Utility class containing format field constants for document validation and formatting.
 */
public class FormatFields {

    /**
     * Key field constant for array primary keys.
     */
    public static final String KEY = "jdocs_arr_pk";

    /**
     * Type field constant for data type specification.
     */
    public static final String TYPE = "type";

    /**
     * Regex field constant for pattern validation.
     */
    public static final String REGEX = "regex";

    /**
     * Format field constant for date formatting.
     */
    public static final String FORMAT = "format";

    /**
     * Null allowed field constant.
     */
    public static final String NULL_ALLOWED = "null_allowed";

    /**
     * Ignore regex if empty string field constant.
     */
    public static final String IGNORE_REGEX_IF_EMPTY_STRING = "ignore_regex_if_empty_string";

    /**
     * Empty date allowed field constant.
     */
    public static final String EMPTY_DATE_ALLOWED = "empty_date_allowed";

    /**
     * Private constructor to prevent instantiation.
     */
    private FormatFields() {
    }

}
