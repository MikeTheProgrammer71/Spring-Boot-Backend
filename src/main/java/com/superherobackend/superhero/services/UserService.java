package com.superherobackend.superhero.services;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superherobackend.superhero.dto.UserDTO;
import com.superherobackend.superhero.models.User;
import com.superherobackend.superhero.models.UserSuperhero;
import com.superherobackend.superhero.repositories.UserRepository;
import com.superherobackend.superhero.repositories.UserSuperheroRepository;
import com.superherobackend.superhero.security.AuthenticationResponse;
import com.superherobackend.superhero.security.JwtUtil;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSuperheroRepository userSuperheroRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public List<UserDTO> getAllUsers() {
        return userRepository.fetchAllUsers();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public AuthenticationResponse authenticateUser(String username, String unhashedPassword) {
        User user = userRepository.findByUsername(username);
    
        if (user != null && BCrypt.checkpw(unhashedPassword, user.getHashedPassword())) {
            String jwtToken = jwtUtil.generateToken(user.getUsername());
            UserDTO userDTO = new UserDTO(user);
            return new AuthenticationResponse(jwtToken, userDTO);
        } 
        
        return null;
    }
    
    public void addUser(String name, String username, String unhashedPassword) {
        String hashedPassword = BCrypt.hashpw(unhashedPassword, BCrypt.gensalt(12));

        User user = new User();
        user.setName(name);
        user.setRole("User");
        user.setUsername(username);
        user.setHashedPassword(hashedPassword);
        
        userRepository.save(user);
    }
    
    public Long getMaxUserId() {
        return userRepository.findMaxUserId();
    }

    public void deleteUserById(Long userId) {
        
        if (userRepository.existsById(userId)) {

            User user = userRepository.findByUserId(userId);

            if (user.getRole().equals("Admin")) {
                throw new RuntimeException("Can't delete Admin user. Login to your database to perform this action");
            }

            // Delete associated UserSuperhero records
            List<UserSuperhero> userSuperheroes = userSuperheroRepository.findByUser_UserId(userId);
            userSuperheroRepository.deleteAll(userSuperheroes);
            
            // Delete the user
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }

}
