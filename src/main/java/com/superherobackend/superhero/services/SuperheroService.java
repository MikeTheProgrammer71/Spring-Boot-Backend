package com.superherobackend.superhero.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.superherobackend.superhero.dto.SuperheroDTO;
import com.superherobackend.superhero.exceptions.DuplicateSuperheroException;
import com.superherobackend.superhero.models.Image;
import com.superherobackend.superhero.models.Power;
import com.superherobackend.superhero.models.Superhero;
import com.superherobackend.superhero.models.User;
import com.superherobackend.superhero.models.UserSuperhero;
import com.superherobackend.superhero.repositories.ImageRepository;
import com.superherobackend.superhero.repositories.PowerRepository;
import com.superherobackend.superhero.repositories.SuperheroRepository;
import com.superherobackend.superhero.repositories.UserRepository;
import com.superherobackend.superhero.repositories.UserSuperheroRepository;
import com.superherobackend.superhero.security.CustomUserDetails;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

@Service
public class SuperheroService {

    @Autowired
    private SuperheroRepository superheroRepository;

    @Autowired
    private PowerRepository powerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSuperheroRepository userSuperheroRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private S3ImageService s3ImageService;
    
    public List<Superhero> getAllSuperheroes() {
        return superheroRepository.findAllSuperheroes();
    }

    @Transactional
    public Superhero addNewSuperhero(SuperheroDTO superheroDTO, Long userId, MultipartFile image) throws Exception {
        Long authenticatedUserId = getAuthenticatedUserId();

        if (!authenticatedUserId.equals(userId)) {
            throw new RuntimeException("You can only add superheroes to your own account.");
        }

        Superhero existingSuperhero = superheroRepository.findByNameAndUserId(superheroDTO.getName(), userId);
        
        if (existingSuperhero != null) {
            throw new DuplicateSuperheroException("Superhero already exists for this user!");
        }

        Superhero superhero = new Superhero();
        superhero.setName(superheroDTO.getName());
        superhero.setRealName(superheroDTO.getRealName());
        superhero.setUniverse(superheroDTO.getUniverse());
        superhero.setYearCreated(superheroDTO.getYearCreated());
        superhero.setCanDelete(superheroDTO.getCanDelete());

        // Set the powers for the superhero
        Set<Power> powers = new HashSet<>();
        powerRepository.findAllById(superheroDTO.getPowerIds()).forEach(powers::add);
        superhero.setPowers(powers);

        // Save the new superhero in db
        Superhero savedSuperhero = superheroRepository.save(superhero);

        // Connect image with superhero
        String storedFilename = s3ImageService.uploadImage(savedSuperhero.getSuperId(), image);


        // Check if an image already exists for the superhero
        Image existingImage = imageRepository.findBySuperhero(savedSuperhero);
        
        if (existingImage != null) {
            existingImage.setOriginalFilename(image.getOriginalFilename());
            existingImage.setStoredFilename(storedFilename);
            imageRepository.save(existingImage);
        } else {
            Image newImage = new Image();
            newImage.setOriginalFilename(image.getOriginalFilename());
            newImage.setStoredFilename(storedFilename);
            newImage.setSuperhero(savedSuperhero);
            imageRepository.save(newImage);
            savedSuperhero.setImage(newImage);
        }

        // Create the association between the superhero and user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        UserSuperhero userSuperhero = new UserSuperhero(user, savedSuperhero);
        userSuperheroRepository.save(userSuperhero);

        return savedSuperhero;
    }

    public List<Superhero> getSuperheroesByPower(Long powerId) {
        return superheroRepository.findByPowerId(powerId);
    }

    @Transactional
    public void deleteSuperhero(Long superId) {
        Long authenticatedUserId = getAuthenticatedUserId();
        boolean isAdmin = isAuthenticatedUserAdmin();

        Superhero superhero = superheroRepository.findById(superId)
                .orElseThrow(() -> new RuntimeException("Superhero not found with ID: " + superId));

        if (!isAdmin) {
            User owner = userSuperheroRepository.findOwnerBySuperheroId(superId);
            if (owner == null || !owner.getUserId().equals(authenticatedUserId) || !superhero.getCanDelete()) {
                throw new RuntimeException("You don't have permission to delete this superhero.");
            }
        }

        String storedFilename = imageRepository.findStoredFilenameBySuperheroId(superId);

        if (storedFilename != null) {
            s3ImageService.deleteImage(storedFilename);
        }

        // Delete the image data from the Images table
        imageRepository.deleteBySuperheroSuperId(superId);

        // Delete all associations between the superhero has with users
        userSuperheroRepository.deleteBySuperhero_SuperId(superId);

        // Delete the superhero and their powers
        superheroRepository.deleteById(superId);
    }

    public Long getMaxSuperId() {
        return superheroRepository.findMaxSuperId();
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
