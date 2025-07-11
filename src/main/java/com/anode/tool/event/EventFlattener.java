package com.anode.tool.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class EventFlattener {
	
	protected DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss") ;
	
	private boolean appendEventType = true ;
	private boolean appendDate = true ;
	private boolean appendNumericalDate = true ;
	
	protected List<String> list ;
	
	public List<String> getFields(Event event)  {
		list = new ArrayList<String>(5) ;
		
		if (appendDate)
			appendDate(event) ;
		
		if (appendNumericalDate)
			appendNumericalDate(event) ;
		
		if (appendEventType)
			appendEventType(event) ;		
		
		return list ;
	}

	private void appendDate(Event event) {
		list.add(dateFormatter.format(event.getDate())) ;
	}

	protected void appendEventType(Event event)  {
		list.add(event.getClass().getSimpleName()) ;		
	}

	public boolean isAppendEventType() {
		return appendEventType;
	}

	public void setAppendEventType(boolean appendEventType) {
		this.appendEventType = appendEventType;
	}
	
	protected void appendNumericalDate(Event event)  {
		list.add(Long.toString(event.getDate().getTime())) ;		
	}

	public boolean isAppendNumericalDate() {
		return appendNumericalDate;
	}
	
	public void setAppendDate(boolean appendDate) {
		this.appendDate = appendDate;
	}

	public void setAppendNumericalDate(boolean appendNumericalDate) {
		this.appendNumericalDate = appendNumericalDate;
	}
	
	public List<String> getFieldnames() {
		List<String> fields = new LinkedList<String>() ;
		
		if (appendDate)
			fields.add("Date") ;
		
		if (appendNumericalDate)
			fields.add("Millisecs. 1970") ;
		
		if (appendEventType)
			fields.add("Type") ;
		
		return fields ;
	}

	public DateFormat getDateFormatter() {
		return dateFormatter;
	}

	public void setDateFormatter(DateFormat dateFormatter) {
		this.dateFormatter = dateFormatter;
	}
	
}
