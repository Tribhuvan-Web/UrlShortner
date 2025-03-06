package com.url.shortner.Exception;

public class CustomAliasAlreadyExistsException extends RuntimeException {
    public CustomAliasAlreadyExistsException() {
        super("Custom alias is already in use");
    }
}
