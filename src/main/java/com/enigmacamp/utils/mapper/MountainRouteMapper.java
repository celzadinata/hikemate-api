package com.enigmacamp.utils.mapper;

import com.enigmacamp.model.dto.request.MountainRouteRequest;
import com.enigmacamp.model.dto.response.MountainRouteResponse;
import com.enigmacamp.model.entity.MountainRoute;
import com.enigmacamp.utils.EntityMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MountainRouteMapper implements EntityMapper<MountainRoute, MountainRouteRequest, MountainRouteResponse> {
    @Override
    public MountainRouteResponse entityToResponse(MountainRoute entity) {
        return MountainRouteResponse.builder()
                .id(entity.getId())
                .mountainId(entity.getMountain().getId())
                .routeId(entity.getRoute().getId())
                .routeName(entity.getRoute().getRoute())
                .build();
    }

    @Override
    public MountainRouteRequest responseToRequest(MountainRouteResponse response) {
        return null;
    }

    @Override
    public MountainRoute requestToEntity(MountainRouteRequest request) {
        return null;
    }

    @Override
    public MountainRoute responseToEntity(MountainRouteResponse response) {
        return null;
    }
}
