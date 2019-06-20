package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.service.EventService;
import com.ksquareinc.calendar.service.NotificationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/event")
@PropertySource("classpath:sec.properties")
public class EventController {

    private final String EVENT_UPDATE_MSG = "Event has been updated successfully. ";
    private final String EVENT_DELETE_MSG = "Event has been deleted successfully. ";
    private final String EVENT_SAVE_MSG = "New Event has been saved with ID: ";
    private final String EVENT_GUEST_ERROR = "There were no valid guest for the event. ";
    private final String EVENT_CREATOR_ERROR = "The creator you specified is not valid for event creation. ";
    @Autowired
    private EventService eventService;

    @Autowired
    private NotificationService notificationService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = EVENT_SAVE_MSG),
            @ApiResponse(code = 422, message = EVENT_GUEST_ERROR),
            @ApiResponse(code = 400, message = EVENT_CREATOR_ERROR)
    })
    @PostMapping
    public ResponseEntity<?> save(@RequestHeader(value = "${tokenName}") String token, @RequestBody Event event){
        if (eventService.isCreatorValidSso(token, event)){
            event = eventService.getWithValidSsoGuests(token, event);
            if (event != null){
                Event newEvent = eventService.save(event);
                notificationService.notifyWebHooks(newEvent);
                return ResponseEntity.ok().body(EVENT_SAVE_MSG + newEvent.toString());
            }else{
                return ResponseEntity.status(422).body(EVENT_GUEST_ERROR);
            }
        }
        return ResponseEntity.badRequest().body(EVENT_CREATOR_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> findOne(@PathVariable("id") long id) {
        Event event = eventService.findOne(id);
        return ResponseEntity.ok().body(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> findAll() {
        List<Event> events = eventService.findAll();
        return ResponseEntity.ok().body(events);
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = EVENT_UPDATE_MSG),
            @ApiResponse(code = 400, message = EVENT_CREATOR_ERROR),
            @ApiResponse(code = 422, message = EVENT_GUEST_ERROR)
    })
    @PutMapping
    public ResponseEntity<?> update(@RequestHeader(value = "${tokenName}") String token, @RequestBody Event event) {
        if (eventService.isCreatorValidSso(token, event)){
            event = eventService.getWithValidSsoGuests(token, event);
            if (event != null){
                eventService.update(event);
                notificationService.notifyWebHooks(event);
                return ResponseEntity.ok().body(EVENT_UPDATE_MSG + event.toString());
            }else{
                return ResponseEntity.status(422).body(EVENT_GUEST_ERROR);
            }
        }
        return ResponseEntity.badRequest().body(EVENT_CREATOR_ERROR);
    }

    /*---Delete a event by id---*/
    @DeleteMapping("/{id}")
    @ApiResponse(code = 200, message = EVENT_UPDATE_MSG)
    public ResponseEntity<?> deleteById(@PathVariable("id") long id) {
        eventService.deleteById(id);
        return ResponseEntity.ok().body(EVENT_DELETE_MSG);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Event event) {
        eventService.delete(event);
        return ResponseEntity.ok().body(EVENT_DELETE_MSG);
    }

    @GetMapping("/byCreator/{id}")
    public ResponseEntity<List<Event>> findByCreator(@PathVariable("id") long creatorId){
        List<Event> allByCreator = eventService.findAllByCreator(creatorId);
        return ResponseEntity.ok().body(allByCreator);
    }

    @GetMapping("/byCreator")
    public ResponseEntity<List<Event>> findByCreator(@RequestParam("username") String username){
        List<Event> allByCreator = eventService.findAllByCreator(username);
        return ResponseEntity.ok().body(allByCreator);
    }

    @GetMapping("/byGuest/{id}")
    public ResponseEntity<List<Event>> findByGuest(@PathVariable("id") long guestId){
        List<Event> allByGuest = eventService.findAllByGuest(guestId);
        return ResponseEntity.ok().body(allByGuest);
    }

    @GetMapping("/byGuest")
    public ResponseEntity<List<Event>> findByGuest(@RequestParam("username") String username){
        List<Event> allByGuest = eventService.findAllByGuest(username);
        return ResponseEntity.ok().body(allByGuest);
    }

    @GetMapping("/byDay")
    public ResponseEntity<List<Event>> findAllByDay(@RequestParam(value = "day")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                            LocalDate day) {
        LocalDateTime dateTime = day.atStartOfDay();
        List<Event> allByDay = eventService.findAllByDay(dateTime);
        return ResponseEntity.ok().body(allByDay);
    }

    @GetMapping("/byWeek/byDay")
    public ResponseEntity<List<Event>> findAllByWeek(@RequestParam(value = "weekday")
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                 LocalDate weekDay) {
        LocalDateTime dateTime = weekDay.atStartOfDay();
        List<Event> allByWeek = eventService.findAllByWeek(dateTime);
        return ResponseEntity.ok().body(allByWeek);
    }

    @GetMapping("/byWeek")
    public ResponseEntity<List<Event>> findAllByWeek(@RequestParam(value = "week") int weekNumber,
                                                     @RequestParam(value = "year") int year) {
        List<Event> allByWeek = eventService.findAllByWeek(weekNumber, year);
        return ResponseEntity.ok().body(allByWeek);
    }

    @GetMapping("/byMonth/byDay")
    public ResponseEntity<List<Event>> findAllByMonth(@RequestParam(value = "monthday")
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                              LocalDate monthDay) {
        LocalDateTime dateTime = monthDay.atStartOfDay();
        List<Event> allByMonth = eventService.findAllByMonth(dateTime);
        return ResponseEntity.ok().body(allByMonth);
    }

    @GetMapping("/byMonth")
    public ResponseEntity<List<Event>> findAllByMonth(@RequestParam(value = "month") int monthNumber,
                                                      @RequestParam(value = "year") int year) {
        List<Event> allByMonth = eventService.findAllByMonth(monthNumber, year);
        return ResponseEntity.ok().body(allByMonth);
    }

}
