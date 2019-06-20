package com.ksquareinc.calendar.service;

import com.ksquareinc.calendar.dao.NotificationDao;
import com.ksquareinc.calendar.model.Customer;
import com.ksquareinc.calendar.model.Event;
import com.ksquareinc.calendar.service.retrofit.WebHookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private NotificationDao notificationDao;

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return notificationDao.create(customer);
    }

    @Override
    @Transactional
    public Customer findOne(long id) {
        return notificationDao.findOne(id);
    }

    @Override
    @Transactional
    public List<Customer> findAll() {
        return notificationDao.findAll();
    }

    @Override
    @Transactional
    public Customer update(Customer customer) {
        return notificationDao.update(customer);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        notificationDao.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(Customer customer) {
        notificationDao.delete(customer);
    }

    @Override
    @Transactional
    public void makePush(long id) {
        notificationDao.makePush(id);
    }

    public void notifyWebHooks(Event event){
        List<Customer> webHooks = findAll();
        for (Customer webHook: webHooks){
            notifyWebHookEndpoint(webHook.getCustomerAPIUrl(), webHook.getEndPoint(), event);
        }
    }

    private void notifyWebHookEndpoint(String baseUrl, String endpoint, Event event){
        Retrofit retrofit;
        try{
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

        }catch (IllegalArgumentException e){
            Logger.getGlobal().warning("Failed notifying API with URL" + baseUrl);
            return;
        }
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
