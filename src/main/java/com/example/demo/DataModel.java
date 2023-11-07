package com.example.demo;

public class DataModel {
    private String email;
    private String password;
    private String additionalEmail;
    private String year;

    public DataModel(String email, String password, String additionalEmail, String year) {
        this.email = email;
        this.password = password;
        this.additionalEmail = additionalEmail;
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAdditionalEmail() {
        return additionalEmail;
    }

    public String getYear() {
        return year;
    }
}