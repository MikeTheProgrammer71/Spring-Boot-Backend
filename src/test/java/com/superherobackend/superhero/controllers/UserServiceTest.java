package com.superherobackend.superhero.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.superherobackend.superhero.models.User;
import com.superherobackend.superhero.repositories.UserRepository;
import com.superherobackend.superhero.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserByUsername() {
        
        String username = "john_doe";
        User mockUser = new User(1L, "John Doe", "User", username, "hashed_password");
        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        User result = userService.getUserByUsername(username);

        assertEquals(mockUser, result);
        verify(userRepository).findByUsername(username);
    }
}
