package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.model.User;
import com.ksquareinc.calendar.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;
    private final int HTTP_BAD_AUTH_STATUS = 403;
    private String BAD_TOKEN_MESSAGE = "Your token is not valid or has expired, try again with a valid token";
    private String BAD_CREATOR_MESSAGE = "The creator of your event is not a valid user";

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Event event, @RequestHeader("token") String token){
        if (SsoController.isTokenValid(token)) {
            if (isCreatorValidSso(event)){
                event = getValidGuestsEvent(event);
                SsoController.deleteToken();
                if (event != null){
                    Event newEvent = eventService.save(event);
                    return ResponseEntity.ok().body("New Event has been saved with ID:" + newEvent.toString());
                }
                return ResponseEntity.badRequest().body("Guests error");
            }
        }
            return ResponseEntity.status(HTTP_BAD_AUTH_STATUS).body(BAD_TOKEN_MESSAGE);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@RequestHeader("token") String token,
                                         @PathVariable("id") long id) {
        if (SsoController.isTokenValid(token)){
            Event event = eventService.findOne(id);
            return ResponseEntity.ok().body(event);
        }
        return ResponseEntity.status(HTTP_BAD_AUTH_STATUS).body(BAD_TOKEN_MESSAGE);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestHeader("token") String token) {
        if (SsoController.isTokenValid(token)){
            List<Event> Events = eventService.findAll();
            return ResponseEntity.ok().body(Events);
        }
        return ResponseEntity.status(HTTP_BAD_AUTH_STATUS).body(BAD_TOKEN_MESSAGE);
    }


    @PutMapping
    public ResponseEntity<?> update(@RequestHeader("token") String token,
                                    @RequestBody Event event) {
        if (SsoController.isTokenValid(token)){
            eventService.update(event);
            return ResponseEntity.ok().body("Event has been updated successfully. \n" + event);
        }
        return ResponseEntity.status(HTTP_BAD_AUTH_STATUS).body(BAD_TOKEN_MESSAGE);
    }

    /*---Delete a event by id---*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@RequestHeader("token") String token,
                                        @PathVariable("id") long id) {
        if (SsoController.isTokenValid(token)) {
            eventService.deleteById(id);
            return ResponseEntity.ok().body("Event has been deleted successfully.");
        }
        return ResponseEntity.status(HTTP_BAD_AUTH_STATUS).body(BAD_TOKEN_MESSAGE);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestHeader("token") String token,
                                    @RequestBody Event event) {
        if (SsoController.isTokenValid(token)) {
            eventService.delete(event);
            return ResponseEntity.ok().body("Event has been deleted successfully.");
        }
        return ResponseEntity.status(HTTP_BAD_AUTH_STATUS).body(BAD_TOKEN_MESSAGE);
    }

    @GetMapping("/byCreator/{id}")
    public ResponseEntity<?> findByCreator(@RequestHeader("token") String token,
                                                     @PathVariable("id") long creatorId){
        if (SsoController.isTokenValid(token)) {
            List<Event> allByCreator = eventService.findAllByCreator(creatorId);
            return ResponseEntity.ok().body(allByCreator);
        }
        return ResponseEntity.status(HTTP_BAD_AUTH_STATUS).body(BAD_TOKEN_MESSAGE);
    }

    @GetMapping("/byCreator")
    public ResponseEntity<?> findByCreator(@RequestHeader("token") String token,
                                                     @RequestParam("username") String username){
        if (SsoController.isTokenValid(token)) {
            List<Event> allByCreator = eventService.findAllByCreator(username);
            return ResponseEntity.ok().body(allByCreator);
        }
        return ResponseEntity.status(HTTP_BAD_AUTH_STATUS).body(BAD_TOKEN_MESSAGE);

    }


    //TODO Implement Token validation for the following methods.
    @GetMapping("/byGuest/{id}")
    public ResponseEntity<List<Event>> findByGuest(@RequestHeader("token") String token,
                                                   @PathVariable("id") long guestId){
        List<Event> allByGuest = eventService.findAllByGuest(guestId);
        return ResponseEntity.ok().body(allByGuest);
    }

    @GetMapping("/byGuest")
    public ResponseEntity<List<Event>> findByGuest(@RequestHeader("token") String token,
                                                   @RequestParam("username") String username){
        List<Event> allByGuest = eventService.findAllByGuest(username);
        return ResponseEntity.ok().body(allByGuest);
    }

    @GetMapping("/byDay")
    public ResponseEntity<List<Event>> findAllByDay(@RequestHeader("token") String token,
                                                    @RequestParam(value = "day")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                            LocalDateTime day) {
        List<Event> allByDay = eventService.findAllByDay(day);
        return ResponseEntity.ok().body(allByDay);
    }

    @GetMapping("/byWeekday")
    public ResponseEntity<List<Event>> findAllByWeek(@RequestHeader("token") String token,
                                                     @RequestParam(value = "weekday")
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                             LocalDateTime weekDay) {
        List<Event> allByWeek = eventService.findAllByWeek(weekDay);
        return ResponseEntity.ok().body(allByWeek);
    }

    @GetMapping("/byWeek")
    public ResponseEntity<List<Event>> findAllByWeek(@RequestParam(value = "week") int weekNumber,
                                                     @RequestParam(value = "year") int year) {
        List<Event> allByWeek = eventService.findAllByWeek(weekNumber, year);
        return ResponseEntity.ok().body(allByWeek);
    }

    @GetMapping("/byMonthday")
    public ResponseEntity<List<Event>> findAllByMonth(@RequestHeader("token") String token,
                                                      @RequestParam(value = "monthday")
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                              LocalDateTime monthDay) {
        List<Event> allByMonth = eventService.findAllByMonth(monthDay);
        return ResponseEntity.ok().body(allByMonth);
    }

    @GetMapping("/byMonth")
    public ResponseEntity<List<Event>> findAllByMonth(@RequestHeader("token") String token,
                                                      @RequestParam(value = "month") int monthNumber,
                                                      @RequestParam(value = "year") int year) {
        List<Event> allByMonth = eventService.findAllByMonth(monthNumber, year);
        return ResponseEntity.ok().body(allByMonth);
    }


    private Event getValidGuestsEvent(Event event){
        List<String> inputUserNames = new ArrayList<>();
        for (User g : event.getGuests()){
            inputUserNames.add(g.getUsername());
        }
        List<String> invalidUserNames = SsoController.validateUserNames(SsoController.appToken.getAccessToken(), inputUserNames);
        if (invalidUserNames == null){
            return null;
        }

        inputUserNames.removeAll(invalidUserNames);
        if (inputUserNames.isEmpty()){
            return null;
        }

        List<User> validGuests = new ArrayList<>();
        for (String name : inputUserNames){
            User guest = new User();
            guest.setUsername(name);
        }
        event.setGuests(validGuests);
        return event;
    }

    private boolean isCreatorValidSso(Event event) {
        List<String> requestList = new ArrayList<>();
        requestList.add(event.getCreator().getUsername());
        List<String> responseList = SsoController.validateUserNames(SsoController.appToken.getAccessToken(), requestList);
        if (responseList == null){
            return false;
        } else{
            return !(responseList.contains(event.getCreator().getUsername()));
        }
    }

}
