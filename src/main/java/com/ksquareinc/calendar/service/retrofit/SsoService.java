package com.ksquareinc.calendar.service.retrofit;

import com.ksquareinc.calendar.model.sso.SsoToken;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SsoService {
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Authorization: Basic Y2FsZW5kYXJJZDpjYWxlbmRhclNlY3JldA=="
    })
    @POST("oauth/token")
    Call<SsoToken> getUserToken(@Body RequestBody requestBody);

    @GET("api/users/auth")
    Call<Void> validateUserToken(@Header("Authorization") String token);

    @Headers({
            "Content-Type: application/json"
    })
    @POST("api/users/areUsers")
    Call<List<String>> validateUsersList(@Header("Authorization") String token,
                                         @Body List<String> userNameList);



}
