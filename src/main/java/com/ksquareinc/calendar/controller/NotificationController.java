package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.Customer;
import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.service.EventService;
import com.ksquareinc.calendar.service.NotificationService;
import com.ksquareinc.calendar.util.UrlValidator;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notification")
public class NotificationController {

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
    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = CUSTOMER_SUCCESS),
            @ApiResponse(code = 422, message = CUSTOMER_URL_ERROR),
            @ApiResponse(code = 422, message = CUSTOMER_ENDPOINT_ERROR),
            @ApiResponse(code = 400, message = CUSTOMER_ERROR)
    })
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer){
        if(customer != null){
            if (!customer.getCustomerAPIUrl().endsWith("/") || !UrlValidator.isUrl(customer.getCustomerAPIUrl())){
                return ResponseEntity.status(422).body(CUSTOMER_URL_ERROR);
            }else if (customer.getEndPoint().endsWith("/") || customer.getEndPoint().startsWith("/")){
                return ResponseEntity.status(422).body(CUSTOMER_ENDPOINT_ERROR);
            }
            Customer c = notificationService.save(customer);
            return ResponseEntity.ok().body(CUSTOMER_SUCCESS + c.toString());
        }

        return ResponseEntity.badRequest().body(CUSTOMER_ERROR);
    }


    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = CUSTOMER_SUCCESS),
            @ApiResponse(code = 422, message = CUSTOMER_URL_ERROR),
            @ApiResponse(code = 422, message = CUSTOMER_ENDPOINT_ERROR),
            @ApiResponse(code = 400, message = CUSTOMER_ERROR)
    })
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer){
        if(customer != null){
            if (!customer.getCustomerAPIUrl().endsWith("/") || !UrlValidator.isUrl(customer.getCustomerAPIUrl())){
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
