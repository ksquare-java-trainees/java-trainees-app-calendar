package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.model.Event;

import java.util.List;

public interface EventService {
    long save(Event event);
    Event get(long id);
    List<Event> list();
    void update(long id, Event event);
    void delete(long id);
}
