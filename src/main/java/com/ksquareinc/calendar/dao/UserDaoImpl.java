package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.User;
import com.ksquareinc.calendar.model.enums.EventsType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao  {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User create(User user) {
        sessionFactory.getCurrentSession().save(user);
        return user;
    }

    @Override
    public User findOne(long id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        Query<User> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public User update(User user) {
        Session session = sessionFactory.getCurrentSession();
        return (User) session.merge(user);
    }

    @Override
    public void deleteById(long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = findOne(id);
        session.delete(user);
    }

    @Override
    public void delete(User user){
        Session session = sessionFactory.getCurrentSession();
        session.delete(user);
    }


    @Override
    public User getByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        String qs = "from com.ksquareinc.calendar.model.User " +
                "where "+ User.USER_USERNAME + " = :username";
        Query<User> query = session.createQuery(qs);
        query.setParameter("username", username);
        List<?> list = query.list();
        if (list.isEmpty()){
            return null;
        }
        return (User) list.get(0);
    }

    @Override
    public User findOneWithUsersList(long id, EventsType eventType) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        root.fetch(eventType.getAttribute(), JoinType.INNER);
        cq.select(root).where(cb.equal(root.get("id"),id)).distinct(true);
        Query<User> query = session.createQuery(cq);
        return query.getSingleResult();
    }


}
