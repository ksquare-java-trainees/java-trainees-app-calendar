package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.controller.SsoController;
import com.ksquareinc.calendar.dao.EventDao;
import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.MIDNIGHT;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDao eventDAO;

    @Autowired
    private UserService userService;

    @Override
    public Event save(Event event) {
        checkCreatorExist(event);
        checkGuestsExist(event);
        return eventDAO.create(event);
    }

    @Override
    public void deleteById(long id) {
        eventDAO.deleteById(id);
    }

    @Transactional
    public void delete(Event event) {
        eventDAO.delete(event);
    }
    @Override
    public Event update(Event event){
        return eventDAO.update(event);
    }

    @Override
    public Event findOne(long id) {
        return eventDAO.findOne(id);
    }

    @Override
    public List<Event> findAll() {
       return eventDAO.findAll();
    }

    private void checkCreatorExist(Event event){
        User databaseUser = userService.findByUsername(event.getCreator().getUsername());
        if (databaseUser != null){
            event.setCreator(databaseUser);
        }else{
            event.setCreator(userService.save(event.getCreator()));
        }
    }

    private void checkGuestsExist(Event event){
       List<User> guests =  event.getGuests();
       for (int i = 0; i < guests.size(); i++){
           User databaseUser = userService.findByUsername(guests.get(i).getUsername());
           if (databaseUser != null){
               guests.set(i, databaseUser);
           }else{
               guests.set(i, userService.save(guests.get(i)));
           }
       }
       event.setGuests(guests);
    }

    @Override
    public List<Event> findAllByCreator(Long creatorId) {
        return userService.findOneWithCreations(creatorId).getEventsCreated();
    }

    @Override
    public List<Event> findAllByCreator(String username) {
        User creator = userService.findByUsername(username);
        return userService.findOneWithCreations(creator.getId()).getEventsCreated();
    }

    @Override
    public List<Event> findAllByGuest(Long guestId) {
        return userService.findOneWithInvitations(guestId).getEventInvitations();
    }

    @Override
    public List<Event> findAllByGuest(String username) {
        User creator = userService.findByUsername(username);
        return userService.findOneWithInvitations(creator.getId()).getEventInvitations();
    }

    @Override
    public List<Event> findAllByDay(LocalDateTime localDateTime) {
        return eventDAO.findAllByDay(localDateTime);
    }


    @Override
    public List<Event> findAllByWeek(LocalDateTime localDateTime) {
        return eventDAO.findAllByWeek(localDateTime);
    }

    @Override
    public List<Event> findAllByWeek(int weekNumber, int year) {
        return eventDAO.findAllByWeek(weekNumber, year);
    }

    @Override
    public List<Event> findAllByMonth(LocalDateTime localDateTime) {
        return eventDAO.findAllByMonth(localDateTime);
    }

    @Override
    public List<Event> findAllByMonth(int monthNumber, int year) {
        return eventDAO.findAllByMonth(monthNumber, year);
    }

    @Override
    public boolean isValid(long eventId) {
        Event testEvent = eventDAO.findOne(eventId);
        if (testEvent == null){
            return false;
        }else if (testEvent.getDateEnd() != null &&
                !testEvent.getIsAllDay()
                && testEvent.getDateEnd().isBefore(LocalDateTime.now())){
            return false;
        }else if (testEvent.getIsAllDay()){
            LocalDateTime nextDay = testEvent.getDateBegin().plusDays(1);
            nextDay = nextDay.with(MIDNIGHT);
            boolean isOver = LocalDateTime.now().isAfter(nextDay);
            return !isOver;
        }
        return true;
    }

    public Event getWithValidSsoGuests(String token, Event event){
        List<String> inputUserNames = new ArrayList<>();
        for (User g : event.getGuests()){
            inputUserNames.add(g.getUsername());
        }
        List<String> invalidUserNames = SsoController.validateUserNames(token, inputUserNames);
        if (invalidUserNames == null){
            return null;
        }

        inputUserNames.removeAll(invalidUserNames);
        if (inputUserNames.isEmpty()){
            return null;
        }

        List<User> validGuests = new ArrayList<>();
        for (String name : inputUserNames){
            User guest = new User();
            guest.setUsername(name);
        }
        event.setGuests(validGuests);
        return event;
    }

    public boolean isCreatorValidSso(String token, Event event) {
        List<String> requestList = new ArrayList<>();
        requestList.add(event.getCreator().getUsername());
        List<String> responseList = SsoController.validateUserNames(token, requestList);
        if (responseList == null){
            return false;
        } else{
            return !(responseList.contains(event.getCreator().getUsername()));
        }
    }

}
