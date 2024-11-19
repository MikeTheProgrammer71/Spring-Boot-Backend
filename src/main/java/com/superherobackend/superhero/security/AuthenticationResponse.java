package com.superherobackend.superhero.security;

import com.superherobackend.superhero.dto.UserDTO;

public class AuthenticationResponse {
    private String jwtToken;
    private UserDTO user;

    public AuthenticationResponse(String jwtToken, UserDTO user) {
        this.jwtToken = jwtToken;
        this.user = user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
