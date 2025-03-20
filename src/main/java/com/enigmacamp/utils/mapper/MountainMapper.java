package com.enigmacamp.utils.mapper;

import com.enigmacamp.constant.enums.MountainStatus;
import com.enigmacamp.model.dto.request.MountainRequest;
import com.enigmacamp.model.dto.response.MountainResponse;
import com.enigmacamp.model.entity.Image;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.utils.EntityMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MountainMapper implements EntityMapper<Mountain, MountainRequest, MountainResponse> {
    @Override
    public MountainResponse entityToResponse(Mountain entity) {
        return MountainResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .location(entity.getLocation())
                .status(entity.getStatus().toString())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .water(entity.getWater())
                .quotaLimit(entity.getQuotaLimit())
                .toilet(entity.getToilet())
                .isOpen(entity.getIsOpen())
                .mountainCoverUrl(entity.getImage() != null? entity.getImage().getPath() : null)
                .baseCampImagesUrl(entity.getBaseCampImages() != null ? entity.getBaseCampImages().stream().map(Image::getPath).toList() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    @Override
    public MountainRequest responseToRequest(MountainResponse response) {
        return MountainRequest.builder()
                .name(response.getName())
                .location(response.getLocation())
                .status(response.getStatus())
                .price(response.getPrice())
                .build();
    }

    @Override
    public Mountain requestToEntity(MountainRequest request) {
        return Mountain.builder()
                .name(request.getName())
                .location(request.getLocation())
                .status(MountainStatus.valueOf(request.getStatus()))
                .price(request.getPrice())
                .description(request.getDescription())
                .water(request.getWater())
                .quotaLimit(request.getQuotaLimit())
                .toilet(request.getToilet())
                .isOpen(request.getIsOpen())
                .build();
    }
}
