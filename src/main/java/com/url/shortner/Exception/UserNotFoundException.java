package com.url.shortner.Exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Email not found");
    }
}
