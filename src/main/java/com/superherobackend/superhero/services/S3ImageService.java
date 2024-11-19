package com.superherobackend.superhero.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.superherobackend.superhero.models.Image;
import com.superherobackend.superhero.models.Superhero;
import com.superherobackend.superhero.repositories.ImageRepository;
import com.superherobackend.superhero.repositories.SuperheroRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
public class S3ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private SuperheroRepository superheroRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${s3.superhero.bucket}")
    private String bucketName;
    

    public String getImageFilenameBySuperhero(Long superId) {
        Superhero superhero = superheroRepository.findById(superId)
                .orElseThrow(() -> new RuntimeException("Superhero not found with ID: " + superId));
    
        Image image = imageRepository.findBySuperhero(superhero);
    
        if (image == null) {
            throw new RuntimeException("Image not found for superhero ID: " + superId);
        }
    
        return image.getStoredFilename();
    }


    public String uploadImage(Long superId, MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
    
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }
    
        String storedFilename = UUID.randomUUID().toString().replace("-", "") + "." + fileExtension;
    
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(storedFilename)
                .build();
    
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    
        Superhero superhero = superheroRepository.findById(superId)
                .orElseThrow(() -> new RuntimeException("Superhero not found with ID: " + superId));
    
        Image existingImage = imageRepository.findBySuperhero(superhero);
        
        if (existingImage != null) {
            existingImage.setOriginalFilename(originalFilename);
            existingImage.setStoredFilename(storedFilename);
            imageRepository.save(existingImage);
        } else {
            // Create a new image if the image doesn't exist
            Image newImage = new Image();

            newImage.setOriginalFilename(originalFilename);
            newImage.setStoredFilename(storedFilename);
            newImage.setSuperhero(superhero);

            imageRepository.save(newImage);
        }
    
        return storedFilename;
    }

    public void deleteImage(String storedFilename) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(storedFilename)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

}
