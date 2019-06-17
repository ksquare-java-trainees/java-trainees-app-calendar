package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.sso.SsoToken;
import com.ksquareinc.calendar.service.retrofit.SsoService;
import com.ksquareinc.calendar.service.retrofit.util.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SsoController {

    private static final String BASE_URL = "http://localhost:8066/ksquare-sso/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private static SsoService service = retrofit.create(SsoService.class);


    static SsoToken appToken;

    public static void generateToken(){
        Call<SsoToken> ssoTokenCall = service.getUserToken(RetrofitUtils.getTokenBody());
        SsoToken ssoToken = null;
        try {
            ssoToken = ssoTokenCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ssoToken == null || ssoToken.getAccessToken().isEmpty()){
            appToken = new SsoToken();
        }else{
            appToken = ssoToken;
        }
    }

    public static boolean isTokenValid(String token){
        try {
            return service.validateUserToken("Bearer "+token).execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    static List<String> validateUserNames(String token, List<String> userNameList){
        try{
            return service.validateUsersList("Bearer "+token,userNameList).execute().body();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    static void deleteToken(){
        appToken = new SsoToken();
    }
}
