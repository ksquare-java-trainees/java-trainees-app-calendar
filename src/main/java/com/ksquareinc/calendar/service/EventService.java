package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService extends GenericService<Event>{

    List<Event> findAllByCreator(Long creatorId);

    List<Event> findAllByCreator(String username);

    List<Event> findAllByGuest(Long guestId);

    List<Event> findAllByGuest(String username);

    List<Event> findAllByDay(LocalDateTime localDateTime);

    List<Event> findAllByWeek(LocalDateTime localDateTime);

    List<Event> findAllByWeek(int weekNumber, int year);

    List<Event> findAllByMonth(LocalDateTime localDateTime);

    List<Event> findAllByMonth(int monthNumber, int year);

    boolean isValid(long eventId);

    Event getWithValidSsoGuests(String token, Event event);

    boolean isCreatorValidSso(String token, Event event);
}
