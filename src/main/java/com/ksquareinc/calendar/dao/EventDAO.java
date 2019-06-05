package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.Event;

import java.util.List;

public interface EventDAO {

    void saveEvent(Event event);

    void deleteEvent(long evID);

    Event getEvent(long evID);

    List<Event> list();
}
