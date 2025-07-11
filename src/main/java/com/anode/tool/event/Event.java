package com.anode.tool.event;

import java.io.Externalizable;
import java.util.Date;
import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class Event implements Comparable<Event> {
    private UUID id;
    private Date date;
}
