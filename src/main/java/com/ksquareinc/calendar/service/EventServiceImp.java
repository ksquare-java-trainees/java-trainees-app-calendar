package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.dao.EventDAO;
import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventServiceImp implements EventService {

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void saveEvent(Event event) {
        checkCreatorExist(event);
        checkGuestsExist(event);
        eventDAO.saveEvent(event);
    }

    @Override
    @Transactional
    public void deleteEvent(long evID) {
        eventDAO.deleteEvent(evID);
    }

    @Override
    @Transactional
    public Event getEvent(long evID) {
        return eventDAO.getEvent(evID);
    }

    @Override
    public List<Event> list() {
       return eventDAO.list();
    }

    private void checkCreatorExist(Event event){
        User databaseUser = userService.getByUsername(event.getCreator().getUsername());
        if (databaseUser != null){
            event.setCreator(databaseUser);
        }
    }

    private void checkGuestsExist(Event event){
       List<User> guests =  event.getGuests();
       for (int i = 0; i < guests.size(); i++){
           User databaseUser = userService.getByUsername(guests.get(i).getUsername());
           if (databaseUser != null){
               guests.set(i, databaseUser);
           }
       }
       event.setGuests(guests);
    }
}
