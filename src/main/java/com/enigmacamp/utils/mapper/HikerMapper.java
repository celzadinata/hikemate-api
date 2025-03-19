package com.enigmacamp.utils.mapper;

import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.response.HikerResponse;
import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.utils.EntityMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HikerMapper implements EntityMapper<Hiker, HikerRequest, HikerResponse> {
    @Override
    public HikerResponse entityToResponse(Hiker entity) {
        return HikerResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    @Override
    public HikerRequest responseToRequest(HikerResponse response) {
        return null;
    }

    @Override
    public Hiker requestToEntity(HikerRequest request) {
        return Hiker.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
}
