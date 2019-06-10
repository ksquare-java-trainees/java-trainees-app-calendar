package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    void saveEvent(Event event);

    void deleteEvent(long evID);

    Event getEvent(long evID);

    List<Event> list();

    List<Event> getByDay(LocalDateTime localDateTime);

    List<Event> getByWeek(LocalDateTime localDateTime);

    List<Event> getByWeek(int weekNumber, int year);

    List<Event> getByMonth(LocalDateTime localDateTime);

    List<Event> getByMonth(int monthNumber, int year);



}
