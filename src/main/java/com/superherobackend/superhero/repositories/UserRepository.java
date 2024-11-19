package com.superherobackend.superhero.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.superherobackend.superhero.dto.UserDTO;
import com.superherobackend.superhero.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByUserId(Long userId);

    @Query("SELECT u FROM User u")
    List<UserDTO> fetchAllUsers();

    @Query("SELECT MAX(u.userId) FROM User u")
    Long findMaxUserId();

}

