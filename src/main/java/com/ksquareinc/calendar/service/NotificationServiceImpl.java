package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.dao.NotificationDao;
import com.ksquareinc.calendar.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private NotificationDao notificationDao;

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return notificationDao.create(customer);
    }

    @Override
    @Transactional
    public Customer findOne(long id) {
        return notificationDao.findOne(id);
    }

    @Override
    @Transactional
    public List<Customer> findAll() {
        return notificationDao.findAll();
    }

    @Override
    @Transactional
    public Customer update(Customer customer) {
        return notificationDao.update(customer);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        notificationDao.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Customer customer) {
        notificationDao.delete(customer);
    }

    @Override
    @Transactional
    public void makePush(long id) {
        notificationDao.makePush(id);
    }
}
