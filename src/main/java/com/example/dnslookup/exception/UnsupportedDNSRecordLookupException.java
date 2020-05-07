package com.evocalize.dnslookup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnsupportedDNSRecordLookupException extends RuntimeException {
    public UnsupportedDNSRecordLookupException(String message) {
        super(message);
    }
}
