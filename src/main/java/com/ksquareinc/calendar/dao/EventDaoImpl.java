package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class EventDaoImpl implements EventDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public Event create(Event event) {
        Session session = sessionFactory.getCurrentSession();
        session.save(event);
        return event;
    }

    @Override
    @Transactional
    public void delete(Event event) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(event);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Event event = findOne(id);
        session.delete(event);
    }

    @Override
    @Transactional
    public Event update(Event event){
        Session session = sessionFactory.getCurrentSession();
        session.merge(event);
        return event;
    }

    @Override
    public Event findOne(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Event.class, id);
    }

    @Override
    public List<Event> findAll() {
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
    public List<Event> findAllByDay(LocalDateTime localDateTime) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        LocalDateTime periodStart = localDateTime.with(LocalTime.MIDNIGHT);
        LocalDateTime periodEnd = periodStart.plusDays(1);
        periodEnd = periodEnd.plusSeconds(-1);
        cq.select(root).where(cb.between(root.get("dateBegin"), periodStart, periodEnd));
        Query<Event> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Event> findAllByWeek(LocalDateTime localDateTime) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        LocalDateTime periodStart = localDateTime.with(LocalTime.MIDNIGHT);
        periodStart = periodStart.with(DayOfWeek.MONDAY);
        LocalDateTime periodEnd = periodStart.plusWeeks(1);
        periodEnd = periodEnd.plusSeconds(-1);
        cq.select(root).where(cb.between(root.get("dateBegin"), periodStart, periodEnd));
        Query<Event> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Event> findAllByWeek(int weekNumber, int year) {
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
    public List<Event> findAllByMonth(LocalDateTime localDateTime) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        LocalDateTime periodStart = localDateTime.with(LocalTime.MIDNIGHT);
        periodStart = periodStart.withDayOfMonth(1);
        LocalDateTime periodEnd = periodStart.plusMonths(1);
        periodEnd = periodEnd.plusSeconds(-1);
        cq.select(root).where(cb.between(root.get("dateBegin"), periodStart, periodEnd));
        Query<Event> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Event> findAllByMonth(int monthNumber, int year) {
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
