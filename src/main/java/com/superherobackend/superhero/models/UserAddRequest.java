package com.superherobackend.superhero.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddRequest {
    private String name;
    private String username;
    private String password;
}
