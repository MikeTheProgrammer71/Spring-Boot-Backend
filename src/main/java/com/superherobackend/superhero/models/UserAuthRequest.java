package com.superherobackend.superhero.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthRequest {
    private String username;
    private String password;
}

