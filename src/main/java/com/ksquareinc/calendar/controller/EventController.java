package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.model.User;
import com.ksquareinc.calendar.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    //Insert into blabla
    @PostMapping("/newEvent")
    public ResponseEntity<?> saveEvent(@RequestBody Event event){
        eventService.saveEvent(event);

        return ResponseEntity.ok().body("New Event has been saved with ID:" + event.toString());
    }

    @DeleteMapping("/{eventID}")
    public ResponseEntity<Event> deleteEvent(@PathVariable("eventID") long evID){
        Event event = eventService.getEvent(evID);
        ResponseEntity<Event> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(event != null){
            eventService.deleteEvent(evID);
            response = new ResponseEntity<>(HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> get(@PathVariable("id") long id) {
        Event event = eventService.getEvent(id);
        return ResponseEntity.ok().body(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> list() {
        List<Event> Events = eventService.list();
        return ResponseEntity.ok().body(Events);
    }


    @GetMapping("/byDay")
    public ResponseEntity<List<Event>> getByDay(@RequestParam(value = "day")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                            LocalDateTime day) {
        List<Event> Events = eventService.getByDay(day);
        return ResponseEntity.ok().body(Events);
    }

    @GetMapping("/byWeekday")
    public ResponseEntity<List<Event>> getByWeek(@RequestParam(value = "weekday")
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                             LocalDateTime weekDay) {
        List<Event> Events = eventService.getByWeek(weekDay);
        return ResponseEntity.ok().body(Events);
    }

    @GetMapping("/byWeek")
    public ResponseEntity<List<Event>> getByWeek(@RequestParam(value = "week") int weekNumber,
                                                 @RequestParam(value = "year") int year) {
        List<Event> Events = eventService.getByWeek(weekNumber, year);
        return ResponseEntity.ok().body(Events);
    }

    @GetMapping("/byMonthday")
    public ResponseEntity<List<Event>> getByMonth(@RequestParam(value = "monthday")
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                              LocalDateTime monthDay) {
        List<Event> Events = eventService.getByMonth(monthDay);
        return ResponseEntity.ok().body(Events);
    }

    @GetMapping("/byMonth")
    public ResponseEntity<List<Event>> getByMonth(@RequestParam(value = "month") int monthNumber,
                                                  @RequestParam(value = "year") int year) {
        List<Event> Events = eventService.getByMonth(monthNumber, year);
        return ResponseEntity.ok().body(Events);
    }




}
