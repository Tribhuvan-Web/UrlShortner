package com.url.shortner.dtos;

import lombok.Data;

@Data
public class LoginRequest {

    private String usernameOrEmail;
    private String password;
}
