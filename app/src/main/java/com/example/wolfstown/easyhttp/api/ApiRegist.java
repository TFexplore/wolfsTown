package com.example.wolfstown.easyhttp.api;

import androidx.annotation.NonNull;

import com.example.wolfstown.modle.Sign;
import com.example.wolfstown.modle.User;
import com.hjq.http.annotation.*;
import com.hjq.http.config.IRequestApi;

public class ApiRegist implements IRequestApi {

    @HttpIgnore
    final String uri="user/register";

    @Query
    String upassword;
    @Query
    String utel;
    @Query
    String uname;

    public ApiRegist(String upassword, String utel, String uname) {
        this.upassword = upassword;
        this.utel = utel;
        this.uname = uname;
    }

    @NonNull
    @Override
    public String getApi() {
        return uri;
    }

    public final static class Bean {
        private User user;
        private Sign token;

        public Sign getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }
    }

}
