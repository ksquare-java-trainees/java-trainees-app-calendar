package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.model.User;

public interface UserService extends GenericService<User>{

    User findOneWithInvitations(long id);

    User findOneWithCreations(long id);

    User findByUsername(String username);
}
