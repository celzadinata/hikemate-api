package com.enigmacamp.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.Url;
import com.enigmacamp.model.entity.Image;
import com.enigmacamp.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;
    @Mock
    private Cloudinary cloudinary;
    @Mock
    private Uploader uplouder;

    private ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageServiceImpl("path", imageRepository);
        ReflectionTestUtils.setField(imageService, "cloudinary", cloudinary);
    }

    @Test
    void testCreateImage() throws IOException {
        final MultipartFile multipartFile = new MockMultipartFile("name", "content".getBytes());
        final Image expectedResult = Image.builder()
                .name("name")
                .path("path")
                .size(0L)
                .contentType("contentType")
                .build();

        when(cloudinary.uploader()).thenReturn(uplouder);

        Map<String, Object> uploadResult = Map.of("public_id", "somePublicId");
        when(uplouder.upload(multipartFile.getBytes(), Map.of("folder", "folderName"))).thenReturn(uploadResult);

        when(cloudinary.url()).thenReturn(new Url(cloudinary));

        final Image image = Image.builder()
                .name("name")
                .path("path")
                .size(0L)
                .contentType("contentType")
                .build();
        when(imageRepository.saveAndFlush(image)).thenReturn(image);

        final Image result = imageService.create(multipartFile, "folderName");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetImage() {
        final Image expectedResult = Image.builder()
                .name("name")
                .path("path")
                .size(0L)
                .contentType("contentType")
                .build();

        final Optional<Image> image = Optional.of(Image.builder()
                .name("name")
                .path("path")
                .size(0L)
                .contentType("contentType")
                .build());
        when(imageRepository.findById("id")).thenReturn(image);

        final Image result = imageService.getImage("id");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetImageImageRepositoryReturnsAbsent() {
        when(imageRepository.findById("id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.getImage("id")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void testDeleteImageById() {
        final Optional<Image> image = Optional.of(Image.builder()
                .name("name")
                .path("path")
                .size(0L)
                .contentType("contentType")
                .build());
        when(imageRepository.findById("id")).thenReturn(image);

        imageService.deleteById("id");

        verify(imageRepository).delete(image.get());
    }

    @Test
    void testDeleteImageByIdImageRepositoryFindByIdReturnsAbsent() {
        when(imageRepository.findById("id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.deleteById("id")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void testRemoveImageFromCloudinary() {
        when(cloudinary.uploader()).thenReturn(uplouder);

        imageService.removeImageFromCloudinary("imagePath");
    }
}
