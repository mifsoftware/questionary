package com.example.testingProject.exception;

import com.fasterxml.jackson.databind.ser.Serializers;

public class UserNotFoundException extends BaseRuntimeException {

    public UserNotFoundException() {
        super("User is not found");
    }
}
