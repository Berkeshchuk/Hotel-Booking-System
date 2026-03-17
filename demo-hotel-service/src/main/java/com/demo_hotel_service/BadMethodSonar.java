package com.demo_hotel_service;

public class BadMethodSonar {
    void badMethod() {
        Object object = null;
        object.toString();
    }

    public void myBadMethod() {
    String dbPassword = "super_secret_password_123"; // SonarQube ненавидить захардкоджені паролі
    System.out.println(dbPassword);
}
}
