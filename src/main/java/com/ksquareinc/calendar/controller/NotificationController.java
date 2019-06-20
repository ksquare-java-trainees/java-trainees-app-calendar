package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Customer;
import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.service.EventService;
import com.ksquareinc.calendar.service.NotificationService;
import com.ksquareinc.calendar.service.retrofit.WebHookService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    EventService eventService;
    @Autowired
    NotificationService notificationService;

    private final String NOTIFY_EVENT_SUCCESS = "The notification has been successfully send.";
    private final String NOTIFY_EVENT_ERROR = "There is no active Event with that ID, Please check your input";
    private final String NOTIFICATION_ERROR = "The information given are not acceptable ";
    private final String NOTIFICATION_SUCCESS = "Your operation was successful ";


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = NOTIFY_EVENT_SUCCESS),
            @ApiResponse(code = 422, message = NOTIFY_EVENT_ERROR)
    })
    @PostMapping("/send/{eventId}")
    public ResponseEntity<?> notifyByEventId(@PathVariable long eventId){
        if (eventService.isValid(eventId)){
            notifyWebHooks(eventService.findOne(eventId));
            return ResponseEntity.ok().body(NOTIFY_EVENT_SUCCESS);
        }
        return ResponseEntity.status(422).body(NOTIFY_EVENT_ERROR);
    }

    @PostMapping("/send")
    public ResponseEntity<?> notifyByEventId(@RequestBody Event event){
        if (event == null || event.getId() == null){
            return ResponseEntity.status(422).body(NOTIFY_EVENT_ERROR);
        }else{
            return notifyByEventId(event.getId());
        }

    }
    @PostMapping
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer){
        Customer c = notificationService.save(customer);
        ResponseEntity<String> response = ResponseEntity.badRequest().body(NOTIFICATION_ERROR);
        if(c != null){
            response = ResponseEntity.ok().body(NOTIFICATION_SUCCESS + c.toString());
        }

        return response;
    }


    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer){
        Customer c = notificationService.save(customer);
        ResponseEntity<String> response = ResponseEntity.badRequest().body(NOTIFICATION_ERROR);
        if(c != null) {
            response = ResponseEntity.ok().body(NOTIFICATION_SUCCESS + c.toString());
        }
        return response;
    }

    public void notifyWebHooks(Event event){
        //TODO Get WebHooks from database;
        Map<String,String> webHooks = new HashMap<>();
        webHooks.put("http://localhost:8080/ksquare-chat/", "notify");
        webHooks.put("http://localhost:8080/ksquare-chat2/", "notify");
        webHooks.forEach((baseUrl, endpoint) -> notifyWebHookEndpoint(baseUrl, endpoint, event));
    }

    private void notifyWebHookEndpoint(String baseUrl, String endpoint, Event event){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        WebHookService webHookService = retrofit.create(WebHookService.class);
        try {
            if(!webHookService.sendNotification(endpoint, event).execute().isSuccessful()){
                Logger.getGlobal().warning("Failed notifying API with URL" + baseUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getGlobal().warning("Failed notifying API with URL" + baseUrl);
        }
    }
}
