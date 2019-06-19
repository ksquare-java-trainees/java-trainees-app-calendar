package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.service.EventService;
import com.ksquareinc.calendar.service.retrofit.WebHookService;
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
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    EventService eventService;

    private static final String NOTIFY_SUCCESS = "Placeholder for notification response object.";
    private static final String EVENT_ERROR = "There is no active Event with that ID, Please check your input";

    @PostMapping("/{eventId}")
    public ResponseEntity<?> notifyByEventId(@PathVariable long eventId){
        if (eventService.isValid(eventId)){
            notifyWebHooks(eventService.findOne(eventId));
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

    public void notifyWebHooks(Event event){
        //TODO Get WebHooks from database;
        Map<String,String> webHooks = new HashMap<>();
        webHooks.put("http://localhost:8080/ksquare-chat/", "notify");
        webHooks.put("http://localhost:8080/ksquare-chat2/", "notify");
        webHooks.forEach((baseUrl, endpoint) -> notifyWebHookEndpoint(baseUrl,endpoint,event));
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
