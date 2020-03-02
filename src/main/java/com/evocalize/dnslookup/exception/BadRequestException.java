package com.evocalize.dnslookup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
    private HttpStatus httpStatus;

    public BadRequestException(String serviceName, HttpStatus status){
        super(String.format(
                "%s reports: '%s' with %s status code",
                serviceName, status.getReasonPhrase(), status.toString()));
    }
}
