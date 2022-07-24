package com.example.wolfstown.easyhttp.api;

import androidx.annotation.NonNull;

import com.example.wolfstown.modle.User;
import com.hjq.http.annotation.HttpIgnore;
import com.hjq.http.annotation.Query;
import com.hjq.http.config.IRequestApi;

public class ApiLogin implements IRequestApi {

    @HttpIgnore
    final String uri="sign/login";

    @Query
    private String Password;
    @Query
    private String Tel;

    public ApiLogin(String password, String tel) {
        Password = password;
        Tel = tel;
    }

    @NonNull
    @Override
    public String getApi() {
        return uri;
    }
    public final static class Bean {
        private User user;
        private String token;

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }
    }
}
