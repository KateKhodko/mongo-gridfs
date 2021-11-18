package com.khodko.mongo.mongogridfs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class MyFileNotFoundException extends ResponseStatusException {

    private String traceId;

    public MyFileNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
        this.traceId = UUID.randomUUID().toString();
    }

    public MyFileNotFoundException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST,message, cause);
        this.traceId = UUID.randomUUID().toString();
    }
}
