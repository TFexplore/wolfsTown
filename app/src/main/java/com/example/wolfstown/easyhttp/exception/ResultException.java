package com.example.wolfstown.easyhttp.exception;

import com.example.wolfstown.easyhttp.model.HttpData;

public class ResultException extends Exception{

    public ResultException(String message, HttpData<?> data) {
        super(message);
    }

}
