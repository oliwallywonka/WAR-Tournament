package com.example.wrsw.models;

public class User {

    //final String id;
    final String email;
    final String password;
    final String name;

    public User(String email,String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;

    }
}
