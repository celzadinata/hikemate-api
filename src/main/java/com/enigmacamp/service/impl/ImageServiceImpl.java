package com.enigmacamp.service.impl;

import com.enigmacamp.model.entity.Image;
import com.enigmacamp.repository.ImageRepository;
import com.enigmacamp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private final Path path;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(@Value("${app.hikemate.upload.path}") String path, ImageRepository imageRepository) {
        this.path = Paths.get(path);
        this.imageRepository = imageRepository;
    }

    @Override
    public Image create(MultipartFile multipartFile) {
        try {
//            if (!List.of("image/jpeg", "image/png", "image/gif", "image/jpg").contains(multipartFile.getContentType())) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File type not supported");
//            }
            String uniqueFileName = System.currentTimeMillis() + "-" + multipartFile.getOriginalFilename();
            Path filePath = path.resolve(uniqueFileName);
            Files.write(filePath, multipartFile.getBytes());

            Image image = Image.builder()
                    .name(multipartFile.getName())
                    .path(filePath.toString())
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .build();

            return imageRepository.saveAndFlush(image);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Override
    public Image getImage(String id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found!"));
    }

    @Override
    public void deleteById(String id) {

    }
}
