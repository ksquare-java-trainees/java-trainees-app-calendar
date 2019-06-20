package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.Customer;

public interface NotificationDao extends GenericDao<Customer> {

    void makePush(long id);
}
