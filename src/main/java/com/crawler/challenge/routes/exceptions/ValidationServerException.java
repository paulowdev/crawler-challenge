package com.crawler.challenge.routes.exceptions;

public class ValidationServerException extends RuntimeException {
    public ValidationServerException(String msg) {
        super(msg);
    }
}
