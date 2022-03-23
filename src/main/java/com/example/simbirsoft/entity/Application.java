package com.example.simbirsoft.entity;

import java.sql.Timestamp;
import java.util.List;

public class Application {
    private long id;
    private String name;
    private Timestamp creationTime;
    private List<Event> events;
}
