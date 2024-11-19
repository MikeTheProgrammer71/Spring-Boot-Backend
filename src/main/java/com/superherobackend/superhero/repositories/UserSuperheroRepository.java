package com.superherobackend.superhero.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.superherobackend.superhero.models.Superhero;
import com.superherobackend.superhero.models.User;
import com.superherobackend.superhero.models.UserSuperhero;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSuperheroRepository extends CrudRepository<UserSuperhero, Long> {
    
    @Query("SELECT s FROM Superhero s JOIN UserSuperhero us ON s.superId = us.superhero.superId WHERE us.user.userId = :userId")
    List<Superhero> findAllSuperheroesByUserId(Long userId);

    @Query("SELECT s FROM Superhero s WHERE s.superId = :superId")
    Optional<Superhero> findSuperheroById(Long superId);

    @Query("SELECT s FROM Superhero s JOIN UserSuperhero us ON s.superId = us.superhero.superId WHERE us.user.userId = :userId AND s.universe = :universe")
    List<Superhero> findSuperheroesByUserIdAndUniverse(Long userId, String universe);

    @Query("SELECT us.user FROM UserSuperhero us WHERE us.superhero.superId = :superId")
    User findOwnerBySuperheroId(@Param("superId") Long superId);

    boolean existsByUserUserIdAndSuperheroSuperId(Long userId, Long superheroId);
    
    void deleteBySuperhero_SuperId(Long superId);

    void deleteByUserUserIdAndSuperheroSuperId(Long userId, Long superheroId);

    @Query("SELECT us.superhero FROM UserSuperhero us JOIN us.superhero.powers p WHERE us.user.userId = :userId AND p.powerId = :powerId")
    List<Superhero> findSuperheroesByUserIdAndPower(Long userId, Long powerId);

    List<UserSuperhero> findByUser_UserId(Long userId);
}
