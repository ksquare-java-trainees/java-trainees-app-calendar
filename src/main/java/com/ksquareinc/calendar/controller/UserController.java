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
    @PostMapping
    public ResponseEntity<?> save(@RequestBody User user) {
        long id = userService.save(user);
        return ResponseEntity.ok().body("New User has been saved with ID:" + id);
    }

    /*---Get a user by id---*/
    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable("id") long id) {
        User user = userService.get(id);
        return ResponseEntity.ok().body(user);
    }

    /*---get all users---*/
    @GetMapping
    public ResponseEntity<List<User>> list() {
        List<User> users = userService.list();
        return ResponseEntity.ok().body(users);
    }

    /*---Update a user by id---*/
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody User user) {
        userService.update(id, user);
        return ResponseEntity.ok().body("User has been updated successfully.");
    }

    /*---Delete a user by id---*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.ok().body("User has been deleted successfully.");
    }
}