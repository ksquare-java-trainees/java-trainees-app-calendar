package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Event event){
        Event newEvent = eventService.save(event);
        return ResponseEntity.ok().body("New Event has been saved with ID:" + newEvent.toString());
    }



    @GetMapping("/{id}")
    public ResponseEntity<Event> findOne(@PathVariable("id") long id) {
        Event event = eventService.findOne(id);
        return ResponseEntity.ok().body(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> findAll() {
        List<Event> Events = eventService.findAll();
        return ResponseEntity.ok().body(Events);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Event event) {
        eventService.update(event);
        return ResponseEntity.ok().body("Event has been updated successfully. \n" + event);
    }

    /*---Delete a event by id---*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") long id) {
        eventService.deleteById(id);
        return ResponseEntity.ok().body("Event has been deleted successfully.");
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Event event) {
        eventService.delete(event);
        return ResponseEntity.ok().body("Event has been deleted successfully.");
    }


    @GetMapping("/byDay")
    public ResponseEntity<List<Event>> findAllByDay(@RequestParam(value = "day")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                            LocalDateTime day) {
        List<Event> Events = eventService.findAllByDay(day);
        return ResponseEntity.ok().body(Events);
    }

    @GetMapping("/byWeekday")
    public ResponseEntity<List<Event>> findAllByWeek(@RequestParam(value = "weekday")
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                             LocalDateTime weekDay) {
        List<Event> Events = eventService.findAllByWeek(weekDay);
        return ResponseEntity.ok().body(Events);
    }

    @GetMapping("/byWeek")
    public ResponseEntity<List<Event>> findAllByWeek(@RequestParam(value = "week") int weekNumber,
                                                     @RequestParam(value = "year") int year) {
        List<Event> Events = eventService.findAllByWeek(weekNumber, year);
        return ResponseEntity.ok().body(Events);
    }

    @GetMapping("/byMonthday")
    public ResponseEntity<List<Event>> findAllByMonth(@RequestParam(value = "monthday")
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                              LocalDateTime monthDay) {
        List<Event> Events = eventService.findAllByMonth(monthDay);
        return ResponseEntity.ok().body(Events);
    }

    @GetMapping("/byMonth")
    public ResponseEntity<List<Event>> findAllByMonth(@RequestParam(value = "month") int monthNumber,
                                                      @RequestParam(value = "year") int year) {
        List<Event> Events = eventService.findAllByMonth(monthNumber, year);
        return ResponseEntity.ok().body(Events);
    }




}
