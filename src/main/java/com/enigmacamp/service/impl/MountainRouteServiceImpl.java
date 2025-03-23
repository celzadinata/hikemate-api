package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.MountainRouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.MountainRoute;
import com.enigmacamp.model.entity.Route;
import com.enigmacamp.repository.MountainRepository;
import com.enigmacamp.repository.MountainRouteRepository;
import com.enigmacamp.repository.RouteRepository;
import com.enigmacamp.service.MountainRouteService;
import com.enigmacamp.utils.specifications.MountainRouteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MountainRouteServiceImpl implements MountainRouteService {
    @Autowired
    private MountainRouteRepository mountainRouteRepository;

    @Autowired
    private MountainRepository mountainRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Override
    public MountainRoute create(MountainRouteRequest request) {
        return mountainRouteRepository.save(MountainRoute.builder().build());
    }

    @Override
    public MountainRoute delete(String id) {
        MountainRoute mountainRoute = findByIdOrThrowNotFound(id);
        mountainRouteRepository.delete(mountainRoute);
        return mountainRoute;
    }

    @Override
    public List<MountainRoute> getAllByMountain(Mountain mountain) {
        return mountainRouteRepository.findMountainRouteByMountain(mountain);
    }

    @Override
    public MountainRoute getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public Page<MountainRoute> getAll(String mountainId, String routeId, SearchRequest searchRequest) {
        Mountain mountain = mountainRepository.findById(mountainId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mountain with id: " + mountainId + " is not found!"));
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route with id: " + routeId + " is not found!"));
        MountainRouteSpecification specification = new MountainRouteSpecification(mountain, route);
        Sort.Direction sortDirection = searchRequest.getDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sortDirection, searchRequest.getSortBy());
        return mountainRouteRepository.findAll(specification, pageable);
    }

    private MountainRoute findByIdOrThrowNotFound(String id) {
        return mountainRouteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MountainRoute with id: " + id + " is not found!"));
    }
}
