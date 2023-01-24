package com.example.testingProject.exception;

import lombok.Getter;

@Getter
public class BaseRuntimeException extends RuntimeException {

    private int status;
    private String error;

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(String message, int status, String error) {
        super(message);
        this.status = status;
        this.error = error;
    }



}
