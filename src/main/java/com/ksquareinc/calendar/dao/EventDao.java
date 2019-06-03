package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.Event;

import java.util.List;

public interface EventDao {
    long save(Event event);

    Event get(long id);

    List<Event> list();

    void update(long id, Event event);

    void delete(long id);

}
