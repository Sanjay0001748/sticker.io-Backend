package com.url.UrlShortner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;


public class LoginRequest {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
