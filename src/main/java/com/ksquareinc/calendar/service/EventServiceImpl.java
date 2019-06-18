package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.dao.EventDao;
import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalTime.MIDNIGHT;

@Service
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDao eventDAO;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Event save(Event event) {
        checkCreatorExist(event);
        checkGuestsExist(event);
        return eventDAO.create(event);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        eventDAO.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Event event) {
        eventDAO.delete(event);
    }
    @Override
    @Transactional
    public Event update(Event event){
        return eventDAO.update(event);
    }

    @Override
    @Transactional
    public Event findOne(long id) {
        return eventDAO.findOne(id);
    }

    @Override
    public List<Event> findAll() {
       return eventDAO.findAll();
    }

    private void checkCreatorExist(Event event){
        User databaseUser = userService.getByUsername(event.getCreator().getUsername());
        if (databaseUser != null){
            event.setCreator(databaseUser);
        }else{
            event.setCreator(userService.save(event.getCreator()));
        }
    }

    private void checkGuestsExist(Event event){
       List<User> guests =  event.getGuests();
       for (int i = 0; i < guests.size(); i++){
           User databaseUser = userService.getByUsername(guests.get(i).getUsername());
           if (databaseUser != null){
               guests.set(i, databaseUser);
           }else{
               guests.set(i, userService.save(guests.get(i)));
           }
       }
       event.setGuests(guests);
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

}
