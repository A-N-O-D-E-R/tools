package com.anode.tool.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract base class for flattening events to a list of string fields.
 */
public abstract class EventFlattener {

    /**
     * Date formatter for converting event dates to strings.
     */
    protected DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Whether to append the event type to the fields.
     */
    private boolean appendEventType = true;

    /**
     * Whether to append the formatted date to the fields.
     */
    private boolean appendDate = true;

    /**
     * Whether to append the numerical date (milliseconds since epoch) to the fields.
     */
    private boolean appendNumericalDate = true;

    /**
     * The list of fields being built.
     */
    protected List<String> list;

    /**
     * Extracts fields from an event.
     * @param event the event to extract fields from
     * @return a list of string representations of the event fields
     */
    public List<String> getFields(Event event) {
		list = new ArrayList<String>(5) ;
		
		if (appendDate)
			appendDate(event) ;
		
		if (appendNumericalDate)
			appendNumericalDate(event) ;
		
		if (appendEventType)
			appendEventType(event) ;		
		
		return list ;
	}

    /**
     * Appends the formatted date of the event to the field list.
     * @param event the event to extract the date from
     */
    private void appendDate(Event event) {
        list.add(dateFormatter.format(event.getDate()));
    }

    /**
     * Appends the event class type to the field list.
     * @param event the event to extract the type from
     */
    protected void appendEventType(Event event) {
        list.add(event.getClass().getSimpleName());
    }

    /**
     * Checks whether the event type is appended to the fields.
     * @return true if event type is appended
     */
    public boolean isAppendEventType() {
        return appendEventType;
    }

    /**
     * Sets whether to append the event type to the fields.
     * @param appendEventType true to append event type
     */
    public void setAppendEventType(boolean appendEventType) {
        this.appendEventType = appendEventType;
    }

    /**
     * Appends the numerical date (milliseconds since epoch) to the field list.
     * @param event the event to extract the date from
     */
    protected void appendNumericalDate(Event event) {
        list.add(Long.toString(event.getDate().getTime()));
    }

    /**
     * Checks whether the numerical date is appended to the fields.
     * @return true if numerical date is appended
     */
    public boolean isAppendNumericalDate() {
        return appendNumericalDate;
    }

    /**
     * Sets whether to append the formatted date to the fields.
     * @param appendDate true to append formatted date
     */
    public void setAppendDate(boolean appendDate) {
        this.appendDate = appendDate;
    }

    /**
     * Sets whether to append the numerical date to the fields.
     * @param appendNumericalDate true to append numerical date
     */
    public void setAppendNumericalDate(boolean appendNumericalDate) {
        this.appendNumericalDate = appendNumericalDate;
    }

    /**
     * Gets the field names corresponding to the extracted fields.
     * @return a list of field name strings
     */
    public List<String> getFieldnames() {
        List<String> fields = new LinkedList<String>();

        if (appendDate)
            fields.add("Date");

        if (appendNumericalDate)
            fields.add("Millisecs. 1970");

        if (appendEventType)
            fields.add("Type");

        return fields;
    }

    /**
     * Gets the date formatter used by this flattener.
     * @return the date formatter
     */
    public DateFormat getDateFormatter() {
        return dateFormatter;
    }

    /**
     * Sets the date formatter used by this flattener.
     * @param dateFormatter the date formatter to use
     */
    public void setDateFormatter(DateFormat dateFormatter) {
        this.dateFormatter = dateFormatter;
    }
	
}
