package com.superherobackend.superhero.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.superherobackend.superhero.services.S3ImageService;

@RestController
@RequestMapping("/images")
public class S3ImageController {

    @Autowired
    private S3ImageService s3ImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("superId") Long superId,
                                              @RequestParam("file") MultipartFile file) {
        try {
            String filename = s3ImageService.uploadImage(superId, file);
            return ResponseEntity.ok("Image uploaded successfully " + filename);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/{superId}")
    public ResponseEntity<String> getImageBySuperhero(@PathVariable Long superId) {
        try {
            String storedFilename = s3ImageService.getImageFilenameBySuperhero(superId);
            
            return ResponseEntity.ok(storedFilename);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
