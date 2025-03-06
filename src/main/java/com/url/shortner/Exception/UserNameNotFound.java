package com.url.shortner.Exception;

public class UserNameNotFound extends RuntimeException {
    public UserNameNotFound(String message) {
        super(message);
    }
}
