package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventDAOImp implements EventDAO{

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public void saveEvent(Event event) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(event);
    }

    @Override
    public void deleteEvent(long evID) {
        Session session = sessionFactory.getCurrentSession();
        Event ev = session.byId(Event.class).load(evID);
        session.delete(ev);
    }

    @Override
    public Event getEvent(long evID) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Event.class, evID);
    }
}
