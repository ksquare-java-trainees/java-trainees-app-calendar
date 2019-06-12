package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    EventService eventService;

    private static final String NOTIFY_SUCCESS = "Placeholder for notification response object.";
    private static final String EVENT_ERROR = "There is no active Event with that ID, Please check your input";

    @PostMapping("/{eventId}")
    public ResponseEntity<?> notifyByEventId(@PathVariable long eventId){
        if (eventService.isValid(eventId)){
            //TODO send notification rest request to chat service
            return ResponseEntity.ok().body(NOTIFY_SUCCESS);
        }

        return ResponseEntity.badRequest().body(EVENT_ERROR);
    }


    @PostMapping
    public ResponseEntity<?> notifyByEventId(@RequestBody Event event){
        if (event == null || event.getId() == null){
            return ResponseEntity.badRequest().body(EVENT_ERROR);
        }else{
            return notifyByEventId(event.getId());
        }

    }



}
