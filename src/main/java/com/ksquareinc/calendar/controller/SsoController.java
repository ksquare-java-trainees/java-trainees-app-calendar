package com.ksquareinc.calendar.controller;

import com.ksquareinc.calendar.model.sso.SsoToken;
import com.ksquareinc.calendar.security.AuthTokenSecurityConfig;
import com.ksquareinc.calendar.service.retrofit.SsoService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class SsoController {
    public static String BASE_URL = AuthTokenSecurityConfig.ssoApiURI;

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private static SsoService service = retrofit.create(SsoService.class);


    static SsoToken appToken;

    public static void generateToken(){
        Call<SsoToken> ssoTokenCall = service.getUserToken(getTokenBody());
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

    static void deleteToken(){
        appToken = new SsoToken();
    }

    public static boolean isTokenValid(String token){
        try {
            return service.validateUserToken("Bearer "+token).execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> validateUserNames(String token, List<String> userNameList){
        try{
            return service.validateUsersList("Bearer "+token,userNameList).execute().body();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static RequestBody getTokenBody(){
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username=crmadmin&password=adminpass&grant_type=password&undefined=");
        return body;
    }
}
