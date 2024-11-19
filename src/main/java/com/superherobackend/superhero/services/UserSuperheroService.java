package com.superherobackend.superhero.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.superherobackend.superhero.models.Superhero;
import com.superherobackend.superhero.models.User;
import com.superherobackend.superhero.models.UserSuperhero;
import com.superherobackend.superhero.repositories.SuperheroRepository;
import com.superherobackend.superhero.repositories.UserRepository;
import com.superherobackend.superhero.repositories.UserSuperheroRepository;
import com.superherobackend.superhero.security.CustomUserDetails;

@Service
public class UserSuperheroService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SuperheroRepository superheroRepository;

    @Autowired
    private UserSuperheroRepository userSuperheroRepository;

    public List<Superhero> getAllSuperheroesByUserId(Long userId) {
        Long authenticatedUserId = getAuthenticatedUserId();
        boolean isAdmin = isAuthenticatedUserAdmin();

        if (!isAdmin && !authenticatedUserId.equals(userId)) {
            throw new RuntimeException("You can only view superheroes for your own account.");
        }

        return userSuperheroRepository.findAllSuperheroesByUserId(userId);
    }


    public void addSuperheroToUser(Long userId, Long superheroId) {
        Long authenticatedUserId = getAuthenticatedUserId();
        boolean isAdmin = isAuthenticatedUserAdmin();

        if (!isAdmin && !authenticatedUserId.equals(userId)) {
            throw new RuntimeException("You can only add superheroes to your own account.");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Superhero superhero = superheroRepository.findById(superheroId)
            .orElseThrow(() -> new RuntimeException("Superhero not found with id: " + superheroId));

        if (userSuperheroRepository.existsByUserUserIdAndSuperheroSuperId(userId, superheroId)) {
            throw new RuntimeException("Superhero already associated with that user");
        }

        UserSuperhero userSuperhero = new UserSuperhero(user, superhero);
        userSuperheroRepository.save(userSuperhero);
    }


    @Transactional
    public void deleteSuperheroFromUser(Long userId, Long superheroId) {
        Long authenticatedUserId = getAuthenticatedUserId();
        boolean isAdmin = isAuthenticatedUserAdmin();

        if (!isAdmin && !authenticatedUserId.equals(userId)) {
            throw new RuntimeException("You can only remove superheroes from your own account.");
        }

        boolean exists = userSuperheroRepository.existsByUserUserIdAndSuperheroSuperId(userId, superheroId);

        if (!exists) {
            throw new RuntimeException("Association between user and superhero not found");
        }

        userSuperheroRepository.deleteByUserUserIdAndSuperheroSuperId(userId, superheroId);
    }
    

    public boolean isUserSuperhero(Long userId, Long superheroId) {
        return userSuperheroRepository.existsByUserUserIdAndSuperheroSuperId(userId, superheroId);
    }

    public Optional<Superhero> getSuperheroById(Long superId) {
        return userSuperheroRepository.findSuperheroById(superId);
    }
    
    private Long getAuthenticatedUserId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    private boolean isAuthenticatedUserAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
            .getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_Admin"));
    }
}