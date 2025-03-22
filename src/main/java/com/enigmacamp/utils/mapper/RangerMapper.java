package com.enigmacamp.utils.mapper;

import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.response.RangerResponse;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.utils.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RangerMapper implements EntityMapper<Ranger, RangerRequest, RangerResponse> {

    @Autowired
    private MountainMapper mountainMapper;

    @Override
    public RangerResponse entityToResponse(Ranger entity) {
        return RangerResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .assignedAt(entity.getAssignedAt())
                .phoneNumber(entity.getPhoneNumber())
                .mountainResponse(entity.getMountain() != null ? mountainMapper.entityToResponse(entity.getMountain()) : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    @Override
    public RangerRequest responseToRequest(RangerResponse response) {
        return RangerRequest.builder()
                .name(response.getName())
                .phoneNumber(response.getPhoneNumber())
                .build();
    }

    @Override
    public Ranger requestToEntity(RangerRequest request) {
        return Ranger.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    @Override
    public Ranger responseToEntity(RangerResponse response) {
        return null;
    }
}
