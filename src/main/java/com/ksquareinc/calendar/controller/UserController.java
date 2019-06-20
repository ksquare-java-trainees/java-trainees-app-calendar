package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.User;
import com.ksquareinc.calendar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    /*---Add new user---*/
//    @PostMapping
    public ResponseEntity<?> save(@RequestBody User user) {
        User newUser = userService.save(user);
        return ResponseEntity.ok().body("New User has been saved with ID:" + newUser.getId());
    }

    /*---Get a user by id---*/
    @GetMapping("/{id}")
    public ResponseEntity<User> findOne(@PathVariable("id") long id) {
        User user = userService.findOne(id);
        return ResponseEntity.ok().body(user);
    }

    /*---find all users---*/
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    /*---Update a user by id---*/
//    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok().body("User has been updated successfully. \n" + user);
    }

    /*---Delete a user by id---*/
//    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().body("User has been deleted successfully.");
    }

//    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody User user) {
        userService.delete(user);
        return ResponseEntity.ok().body("User has been deleted successfully.");
    }
}