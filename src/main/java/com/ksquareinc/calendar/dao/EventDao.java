package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventDao extends GenericDao<Event> {

    List<Event> findAllByDay(LocalDateTime localDateTime);

    List<Event> findAllByWeek(LocalDateTime localDateTime);

    List<Event> findAllByWeek(int weekNumber, int year);

    List<Event> findAllByMonth(LocalDateTime localDateTime);

    List<Event> findAllByMonth(int monthNumber, int year);
}
