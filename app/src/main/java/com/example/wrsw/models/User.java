package com.example.wrsw.models;

public class User {

    String id;
    String email;
    String password;
    String name;
    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
