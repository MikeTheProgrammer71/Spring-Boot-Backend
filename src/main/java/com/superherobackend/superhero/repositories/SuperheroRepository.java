package com.superherobackend.superhero.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.superherobackend.superhero.models.Superhero;

@Repository
public interface SuperheroRepository extends CrudRepository<Superhero, Long> {
    Superhero findByName(String name);

    @Query("SELECT s FROM Superhero s")
    List<Superhero> findAllSuperheroes();

    @Query("SELECT s FROM Superhero s JOIN UserSuperhero us ON s.superId = us.superhero.superId WHERE s.name = :name AND us.user.userId = :userId")
    Superhero findByNameAndUserId(@Param("name") String name, @Param("userId") Long userId);
    
    @Query("SELECT s FROM Superhero s JOIN s.powers p WHERE p.powerId = :powerId")
    List<Superhero> findByPowerId(@Param("powerId") Long powerId);

    @Query("SELECT MAX(s.superId) FROM Superhero s")
    Long findMaxSuperId();
}
