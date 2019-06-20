package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.model.Customer;

public interface NotificationService extends GenericService<Customer> {

    void makePush(long id);
}
