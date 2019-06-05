package com.ksquareinc.calendar.dao;

import com.ksquareinc.calendar.model.User;

import java.util.List;

public interface UserDao {
    long save(User user);

    User get(long id);

    List<User> list();

    void update(long id, User user);

    void delete(long id);

    User getByUsername(String username);

}
