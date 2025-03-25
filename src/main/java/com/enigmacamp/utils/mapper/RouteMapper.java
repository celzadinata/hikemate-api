package com.enigmacamp.utils.mapper;

import com.enigmacamp.model.dto.request.RouteRequest;
import com.enigmacamp.model.dto.response.RouteResponse;
import com.enigmacamp.model.entity.Route;
import com.enigmacamp.utils.EntityMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteMapper implements EntityMapper<Route, RouteRequest, RouteResponse> {
    @Override
    public RouteResponse entityToResponse(Route entity) {
        return RouteResponse.builder()
                .id(entity.getId())
                .routeName(entity.getRoute())
                .build();
    }

    @Override
    public RouteRequest responseToRequest(RouteResponse response) {
        return null;
    }

    @Override
    public Route requestToEntity(RouteRequest request) {
        return null;
    }

    @Override
    public Route responseToEntity(RouteResponse response) {
        return null;
    }
}
