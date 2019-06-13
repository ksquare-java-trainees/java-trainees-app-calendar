package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.dao.UserDao;
import com.ksquareinc.calendar.model.User;
import com.ksquareinc.calendar.model.enums.EventsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public User save(User user) {
        return userDao.create(user);
    }

    @Override
    @Transactional
    public User findOne(long id) {
        return userDao.findOne(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Transactional
    @Override
    public User update(User user) {
        return userDao.update(user);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        userDao.deleteById(id);
    }


    @Transactional
    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    public User findByUsername(String username){
        return userDao.getByUsername(username);
    }

    @Transactional
    @Override
    public User findOneWithInvitations(long id) {
        return userDao.findOneWithUsersList(id, EventsType.INVITATIONS);
    }

    @Transactional
    @Override
    public User findOneWithCreations(long id) {
        return userDao.findOneWithUsersList(id, EventsType.CREATIONS);
    }


}
