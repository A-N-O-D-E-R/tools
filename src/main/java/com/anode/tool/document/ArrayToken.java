package com.anode.tool.document;

/**
 * Token representing an array element in a path expression.
 */
class ArrayToken extends Token {

    /**
     * Enumeration of filter types for array tokens.
     */
    public enum FilterType {
        /**
         * Filter by name-value pair.
         */
        NAME_VALUE,

        /**
         * Filter by index.
         */
        INDEX,

        /**
         * No filter (empty array).
         */
        EMPTY
    }

    /**
     * Inner class representing a filter for array elements.
     */
    public class Filter {

        /**
         * The type of filter.
         */
        private FilterType type = null;

        /**
         * The field name for name-value filters.
         */
        private String field = null;

        /**
         * The field value for name-value filters.
         */
        private String value = null;

        /**
         * The array index for index filters.
         */
        private int index = -1;

        /**
         * Constructs a name-value filter.
         * @param field the field name
         * @param value the field value
         */
        public Filter(String field, String value) {
            this.field = field;
            this.value = value;
            type = FilterType.NAME_VALUE;
        }

        /**
         * Constructs an index filter.
         * @param index the array index
         */
        public Filter(int index) {
            this.index = index;
            type = FilterType.INDEX;
        }

        /**
         * Constructs an empty filter.
         */
        public Filter() {
            type = FilterType.EMPTY;
        }

        /**
         * Gets the filter type.
         * @return the filter type
         */
        public FilterType getType() {
            return type;
        }

        /**
         * Gets the field name for name-value filters.
         * @return the field name
         */
        public String getField() {
            return field;
        }

        /**
         * Gets the field value for name-value filters.
         * @return the field value
         */
        public String getValue() {
            return value;
        }

        /**
         * Gets the array index for index filters.
         * @return the array index
         */
        public int getIndex() {
            return index;
        }

    }

    /**
     * The filter for this array token.
     */
    private Filter filter = null;

    /**
     * Constructs an ArrayToken with a name-value filter.
     * @param name the name of the array
     * @param field the field name to filter by
     * @param value the value to match
     * @param isLeaf whether this is a leaf token
     */
    public ArrayToken(String name, String field, String value, boolean isLeaf) {
        super(name, isLeaf);
        filter = new Filter(field, value);
    }

    /**
     * Constructs an ArrayToken with an index filter.
     * @param name the name of the array
     * @param index the index to access
     * @param isLeaf whether this is a leaf token
     */
    public ArrayToken(String name, int index, boolean isLeaf) {
        super(name, isLeaf);
        filter = new Filter(index);
    }

    /**
     * Constructs an ArrayToken with no filter.
     * @param name the name of the array
     * @param isLeaf whether this is a leaf token
     */
    public ArrayToken(String name, boolean isLeaf) {
        super(name, isLeaf);
        filter = new Filter();
    }

    /**
     * Indicates this token represents an array element.
     * @return true
     */
    @Override
    public boolean isArray() {
        return true;
    }

    /**
     * Gets the filter for this array token.
     * @return the filter
     */
    public Filter getFilter() {
        return filter;
    }

}
