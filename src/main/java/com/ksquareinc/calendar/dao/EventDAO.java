package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.Event;

public interface EventDAO {

    void saveEvent(Event event);

    void deleteEvent(long evID);

    Event getEvent(long evID);
}
