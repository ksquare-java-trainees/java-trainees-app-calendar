package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.model.User;
import com.ksquareinc.calendar.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    //Insert into blabla
    @PostMapping("/newEvent")
    public ResponseEntity<Void> saveEvent(@RequestBody Event event){
        eventService.saveEvent(event);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/{eventID}")
    public ResponseEntity<Event> deleteEvent(@PathVariable("eventID") long evID){
        Event event = eventService.getEvent(evID);
        ResponseEntity<Event> response = new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
        if(event != null){
            eventService.deleteEvent(evID);
            response = new ResponseEntity<Event>(HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> get(@PathVariable("id") long id) {
        Event event = eventService.getEvent(id);
        return ResponseEntity.ok().body(event);
    }
}
