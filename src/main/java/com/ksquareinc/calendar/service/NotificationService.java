package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.model.Customer;
import com.ksquareinc.calendar.model.Event;

public interface NotificationService extends GenericService<Customer> {

    void makePush(long id);

    void notifyWebHooks(Event event);

}
