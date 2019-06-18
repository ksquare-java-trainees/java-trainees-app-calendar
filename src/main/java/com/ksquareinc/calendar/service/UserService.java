package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.model.User;

import java.util.List;

public interface UserService extends GenericService<User>{
    User getByUsername(String username);
}
