package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.entity.Image;
import com.enigmacamp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(path = APIUrl.IMAGE_API)
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageDynamicType(@PathVariable String id) {
        Image image = imageService.getImage(id);
        System.out.println("Image path: " + image.getPath());
        try {

            Path imagePath = Paths.get(image.getPath());
            byte[] imageBytes = Files.readAllBytes(imagePath);

            MediaType mediaType = MediaType.parseMediaType(image.getContentType());

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving image", e);
        }
    }
}
