package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.User;
import com.ksquareinc.calendar.model.enums.EventsType;

import java.util.List;

public interface UserDao extends GenericDao<User>{

    User findOneWithUsersList(long id, EventsType eventsType);

    User getByUsername(String username);

}
