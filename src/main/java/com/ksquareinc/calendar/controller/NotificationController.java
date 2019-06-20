package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Customer;
import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.service.EventService;
import com.ksquareinc.calendar.service.NotificationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.omg.CORBA.BAD_CONTEXT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        }

        return response;
    }



}
