package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.RouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.RouteResponse;
import com.enigmacamp.model.entity.Route;
import org.springframework.data.domain.Page;

public interface RouteService {
    RouteResponse create(RouteRequest request);
    Page<RouteResponse> getAll(SearchRequest searchRequest);
    RouteResponse getById(String id);
    RouteResponse update(RouteRequest request);
    RouteResponse delete(String id);
    Route getByIdEntity(String id);
}
