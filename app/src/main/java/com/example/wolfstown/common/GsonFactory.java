package com.example.wolfstown.common;

import com.google.gson.Gson;

public class GsonFactory {
    private static Gson gson=new Gson();




    public static Gson getSingletonGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
