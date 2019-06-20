package com.ksquareinc.calendar.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidator {

    public static boolean isUrl(String s){
        try {
            new URL(s);
        }catch (MalformedURLException mue){
            return false;
        }
        return true;
    }
}
