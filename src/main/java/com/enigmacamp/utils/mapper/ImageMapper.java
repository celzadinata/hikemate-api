package com.enigmacamp.utils.mapper;

import com.enigmacamp.model.dto.request.ImageRequest;
import com.enigmacamp.model.dto.response.ImageResponse;
import com.enigmacamp.model.entity.Image;
import com.enigmacamp.utils.EntityMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageMapper implements EntityMapper<Image, ImageRequest, ImageResponse> {
    @Override
    public ImageResponse entityToResponse(Image entity) {
        return ImageResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .path(entity.getPath())
                .size(entity.getSize())
                .contentType(entity.getContentType())
                .build();
    }

    @Override
    public ImageRequest responseToRequest(ImageResponse response) {
        return null;
    }

    @Override
    public Image requestToEntity(ImageRequest request) {
        return null;
    }

    @Override
    public Image responseToEntity(ImageResponse response) {
        return Image.builder()
                .id(response.getId())
                .name(response.getName())
                .path(response.getPath())
                .size(response.getSize())
                .contentType(response.getContentType())
                .build();
    }
}
