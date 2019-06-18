package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.User;

import java.util.List;

public interface UserDao extends GenericDao<User>{

    User getByUsername(String username);

}
