package com.url.shortner.Exception;

public class UserNameAlreadyExists extends RuntimeException {
    public UserNameAlreadyExists(String message) {
        super(message);
    }

}
