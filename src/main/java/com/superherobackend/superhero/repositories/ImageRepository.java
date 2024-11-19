package com.superherobackend.superhero.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.superherobackend.superhero.models.Image;
import com.superherobackend.superhero.models.Superhero;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    
    Image findBySuperhero(Superhero superhero);

    void deleteBySuperheroSuperId(Long superheroId);

    @Query("SELECT i.storedFilename FROM Image i WHERE i.superhero.id = :superheroId")
    String findStoredFilenameBySuperheroId(@Param("superheroId") Long superheroId);

}   


