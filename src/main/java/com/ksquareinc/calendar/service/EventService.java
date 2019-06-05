package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.model.Event;

import java.util.List;

public interface EventService {

    void saveEvent(Event event);

    void deleteEvent(long evID);

    Event getEvent(long evID);

    List<Event> list();

}
