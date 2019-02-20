package com.manuguerra.homebigapp.datamodels;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public static String id,username,password,accessToken,email;

    public User (JSONObject jsonObject,String accessToken){

        try {
            this.email=jsonObject.getString("email");
            this.id=jsonObject.getString("id");
            this.password=jsonObject.getString("password");
            this.accessToken=accessToken;
            this.username=jsonObject.getString("username");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
