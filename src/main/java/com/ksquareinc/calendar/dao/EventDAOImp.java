package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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

    @Override
    public List<Event> list() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        cq.select(root);
        Query<Event> query = session.createQuery(cq);
        return query.getResultList();

    }
}
