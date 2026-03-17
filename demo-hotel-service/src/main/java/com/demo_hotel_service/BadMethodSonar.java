package com.demo_hotel_service;

public class BadMethodSonar {
     
    void badMethod() {
        Object object = null;
        object.toString();
    }
}
