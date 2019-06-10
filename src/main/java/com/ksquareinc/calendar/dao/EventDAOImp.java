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
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Override
    @SuppressWarnings("Duplicates")
    public List<Event> getByDay(LocalDateTime localDateTime) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        LocalDateTime periodStart = localDateTime.with(LocalTime.of(0,0, 0,0));
        LocalDateTime periodEnd = periodStart.plusDays(1);
        periodEnd = periodEnd.plusSeconds(-1);
        cq.select(root).where(cb.between(root.get("dateBegin"), periodStart, periodEnd));
        Query<Event> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Event> getByWeek(LocalDateTime localDateTime) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        LocalDateTime periodStart = localDateTime.with(LocalTime.of(0,0, 0,0));
        periodStart = periodStart.with(DayOfWeek.MONDAY);
        LocalDateTime periodEnd = periodStart.plusWeeks(1);
        periodEnd = periodEnd.plusSeconds(-1);
        cq.select(root).where(cb.between(root.get("dateBegin"), periodStart, periodEnd));
        Query<Event> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Event> getByWeek(int weekNumber, int year) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        LocalDateTime periodStart = LocalDateTime.of(year, 1, 1, 0, 0).plusWeeks(weekNumber-1);
        periodStart = periodStart.with(DayOfWeek.MONDAY);
        LocalDateTime periodEnd = periodStart.plusWeeks(1);
        periodEnd = periodEnd.plusSeconds(-1);
        cq.select(root).where(cb.between(root.get("dateBegin"), periodStart, periodEnd));
        Query<Event> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Event> getByMonth(LocalDateTime localDateTime) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        LocalDateTime periodStart = localDateTime.with(LocalTime.of(0,0, 0,0));
        periodStart = periodStart.withDayOfMonth(1);
        LocalDateTime periodEnd = periodStart.plusMonths(1);
        periodEnd = periodEnd.plusSeconds(-1);
        cq.select(root).where(cb.between(root.get("dateBegin"), periodStart, periodEnd));
        Query<Event> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Event> getByMonth(int monthNumber, int year) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        LocalDateTime periodStart = LocalDateTime.of(year, 1, 1, 0, 0).plusMonths(monthNumber-1);
        LocalDateTime periodEnd = periodStart.plusMonths(1);
        periodEnd = periodEnd.plusSeconds(-1);
        cq.select(root).where(cb.between(root.get("dateBegin"), periodStart, periodEnd));
        Query<Event> query = session.createQuery(cq);
        return query.getResultList();
    }
}
