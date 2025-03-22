package com.enigmacamp.service;

import com.enigmacamp.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image create(MultipartFile image, String folderName);
    Image getImage(String id);
    void deleteById(String id);
    void removeImageFromCloudinary(String imagePath);
}
