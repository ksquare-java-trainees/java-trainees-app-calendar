package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.dao.EventDAO;
import com.ksquareinc.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EventServiceImp implements EventService {

    @Autowired
    private EventDAO eventDAO;

    @Override
    @Transactional
    public void saveEvent(Event event) {
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
}
