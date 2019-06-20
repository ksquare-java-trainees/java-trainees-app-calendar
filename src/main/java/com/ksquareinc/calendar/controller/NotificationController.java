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
@RequestMapping("/notification")
public class NotificationController {

    private final String VALID_URL_REGEX = "(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$";
    @Autowired
    EventService eventService;
    @Autowired
    NotificationService notificationService;

    private final String NOTIFY_EVENT_SUCCESS = "The notification has been successfully send.";
    private final String NOTIFY_EVENT_ERROR = "There is no active Event with that ID, Please check your input";
    private final String CUSTOMER_ERROR = "The information given was not acceptable ";
    private final String CUSTOMER_SUCCESS = "Your operation was successful ";
    private final String CUSTOMER_URL_ERROR = CUSTOMER_ERROR + ". The format for your API url must be as following: 'http://yoursite.com/yourapi/'. (Including a slash at the end) ";
    private final String CUSTOMER_ENDPOINT_ERROR = CUSTOMER_ERROR + ". The format for your API url must be as following: 'getNotification' or 'api/calendarNotification'. (Without slashes at the start or end) ";


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = NOTIFY_EVENT_SUCCESS),
            @ApiResponse(code = 422, message = NOTIFY_EVENT_ERROR)
    })
    @PostMapping("/send/{eventId}")
    public ResponseEntity<?> notifyByEventId(@PathVariable long eventId){
        if (eventService.isValid(eventId)){
            notificationService.notifyWebHooks(eventService.findOne(eventId));
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
        if(customer != null){
            if (!customer.getCustomerAPIUrl().endsWith("/") || !customer.getCustomerAPIUrl()
                    .matches(VALID_URL_REGEX)){
                return ResponseEntity.status(422).body(CUSTOMER_URL_ERROR);
            }
            if (customer.getEndPoint().endsWith("/") || customer.getEndPoint().startsWith("/")){
                return ResponseEntity.status(422).body(CUSTOMER_ENDPOINT_ERROR);
            }
            Customer c = notificationService.save(customer);
            return ResponseEntity.ok().body(CUSTOMER_SUCCESS + c.toString());
        }

        return ResponseEntity.badRequest().body(CUSTOMER_ERROR);
    }


    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer){
        if(customer != null){
            if (customer.getCustomerAPIUrl().endsWith("/") && !customer.getCustomerAPIUrl()
                    .matches(VALID_URL_REGEX)){
                return ResponseEntity.status(422).body(CUSTOMER_URL_ERROR);
            }else if (customer.getEndPoint().endsWith("/") || customer.getEndPoint().startsWith("/")){
                return ResponseEntity.status(422).body(CUSTOMER_ENDPOINT_ERROR);
            }else {
                Customer c = notificationService.update(customer);
                return ResponseEntity.ok().body(CUSTOMER_SUCCESS + c.toString());
            }
        }
        return ResponseEntity.badRequest().body(CUSTOMER_ERROR);
    }


}
