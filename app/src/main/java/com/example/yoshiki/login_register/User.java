package com.example.yoshiki.login_register;

/**
 * Created by Yoshiki on 2016/01/02.
 */
public class User {

    String name, username, password;
    int age;

    public User(String username, String password, String name, int age){
        this.username = username;
        this.password = password;
        this.name = name;
        this.age  = age;
    }

    public User(String username,String password){
        this.username = username;
        this.password = password;
        this.name = "";
        this.age  = -1;
    }
}
