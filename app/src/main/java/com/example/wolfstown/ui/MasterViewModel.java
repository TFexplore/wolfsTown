package com.example.wolfstown.ui;

import androidx.lifecycle.ViewModel;

import com.example.wolfstown.modle.User;

public class MasterViewModel extends ViewModel {
    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
