package com.ksquareinc.calendar.dao;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import com.ksquareinc.calendar.model.Customer;
import java.util.List;

@Repository
public class NotificationDaoImpl implements NotificationDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Customer create(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.save(customer);
        return customer;
    }

    @Override
    @Transactional
    public Customer findOne(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Customer.class, id);
    }

    @Override
    @Transactional
    public List<Customer> findAll() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> root = cq.from(Customer.class);
        cq.select(root);
        Query<Customer> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Customer update(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(customer);
        return customer;
    }

    @Override
    @Transactional
    public void delete(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(customer);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(findOne(id));
    }

    @Override
    @Transactional
    public void makePush(long id) {
        Session session = sessionFactory.getCurrentSession();

    }
}
