package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Customer;
import com.ksquareinc.calendar.model.Event;
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

    private final String BAD_REQUEST = "The information given are not acceptable ";
    private final String OK = "Your operation was successful ";

    @Autowired
    NotificationService notificationService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK),
            @ApiResponse(code = 400, message = BAD_REQUEST)
    })

    @PostMapping
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer){
        Customer c = notificationService.save(customer);
        ResponseEntity<String> response = ResponseEntity.badRequest().body(BAD_REQUEST);
        if(c != null){
            response = ResponseEntity.ok().body(OK + c.toString());
        }

        return response;
    }


    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer){
        Customer c = notificationService.save(customer);
        ResponseEntity<String> response = ResponseEntity.badRequest().body(BAD_REQUEST);
        if(c != null){
            response = ResponseEntity.ok().body(OK + c.toString());

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
