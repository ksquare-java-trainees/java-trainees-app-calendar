package com.ksquareinc.calendar.service.retrofit;

import com.ksquareinc.calendar.model.Event;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface WebHookService {
    @POST
    Call<Void> sendNotification(@Url String url, @Body Event event);
}
