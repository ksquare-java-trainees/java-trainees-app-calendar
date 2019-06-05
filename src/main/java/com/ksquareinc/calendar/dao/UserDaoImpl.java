package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.User;
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
public class UserDaoImpl implements UserDao  {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public long save(User user) {
        sessionFactory.getCurrentSession().save(user);
        return user.getId();
    }

    @Override
    public User get(long id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Override
    public List<User> list() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        Query<User> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public void update(long id, User user) {
        Session session = sessionFactory.getCurrentSession();
        User user2 = session.byId(User.class).load(id);
        user2.setUsername(user.getUsername());
        user2.setSsoId(user.getSsoId());
        user2.setToken(user.getToken());
        user2.setEventsCreated(user.getEventsCreated());
        user2.setEventInvitations(user.getEventInvitations());
        session.flush();
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.byId(User.class).load(id);
        session.delete(user);
    }


    @Override
    public User getByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        String qs = "from com.ksquareinc.calendar.model.User " +
                "where com.ksquareinc.calendar.model.User.USER_USERNAME = :username";
        Query<User> query = session.createQuery(qs);
        query.setParameter("username", username);
        List<?> list = query.list();
        if (list.isEmpty()){
            return null;
        }
        return (User) list.get(0);
    }
}
