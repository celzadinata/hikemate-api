package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.RouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.RouteResponse;
import com.enigmacamp.model.entity.Route;
import com.enigmacamp.repository.RouteRepository;
import com.enigmacamp.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Override
    public RouteResponse create(RouteRequest request) {
        Route route = routeRepository.save(Route.builder().route(request.getRouteName()).build());

        return RouteResponse.builder()
                .id(route.getId())
                .routeName(route.getRoute())
                .build();
    }

    @Override
    public Page<RouteResponse> getAll(SearchRequest searchRequest) {
        Sort.Direction sortDirection = searchRequest.getDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sortDirection, searchRequest.getSortBy());
        return routeRepository.findAll(pageable).map(item -> RouteResponse.builder()
                .id(item.getId())
                .routeName(item.getRoute())
                .build());
    }

    @Override
    public RouteResponse getById(String id) {
        Route route = findByIdOrThrowNotFound(id);
        return RouteResponse.builder()
                .id(route.getId())
                .routeName(route.getRoute())
                .build();
    }

    @Override
    public Route getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    private Route findByIdOrThrowNotFound(String id){
        return routeRepository.findById(id).orElseThrow(() -> new RuntimeException("Route not found!", new RuntimeException("Route not found!", new Throwable())));
    }
}
