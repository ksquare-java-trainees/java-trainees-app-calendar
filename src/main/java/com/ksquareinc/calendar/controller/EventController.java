package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Event event){
        /*RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response;
        final String authURI = "http://192.168.240.253:8080/ksquare-sso/api/users/auth";
        final String authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjA1Mjg5NTYsInVzZXJfbmFtZSI6ImNybWFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiJdLCJqdGkiOiI2MzQ3ZjU0My1jY2ExLTQ1ZGItODYyNS01MGZkYjI0NjI2M2IiLCJjbGllbnRfaWQiOiJjYWxlbmRhcklkIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXX0.HQfsJRI1g1WVFn46RV8zFErsCZDQMMxkXstP-fIwjgQ";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        try{
            response = rt.exchange(authURI, HttpMethod.GET, entity, String.class);
        }catch (HttpClientErrorException e) {
            response = ResponseEntity.status(401).body("Invalid Token");
        }

        Event newEvent = eventService.save(event);

        return response;*/
        Event newEvent = eventService.save(event);
        return ResponseEntity.ok().body(newEvent);

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
