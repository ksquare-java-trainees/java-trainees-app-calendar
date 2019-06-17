package com.ksquareinc.calendar.service.retrofit.util;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

import java.io.IOException;
import java.util.Base64;

public class RetrofitUtils {

    private static String AUTH_USER = "calendarId";
    private static String AUTH_SECRET = "calendarSecret";


    public static String basicAuth(){
        return (Base64.getEncoder().encodeToString( (AUTH_USER + ":" + AUTH_SECRET).getBytes()));
    }

    public static RequestBody getTokenBody(){
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username=crmadmin&password=adminpass&grant_type=password&undefined=");
        return body;
    }

}
