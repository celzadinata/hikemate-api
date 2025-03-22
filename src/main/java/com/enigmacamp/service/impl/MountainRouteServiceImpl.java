package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.MountainRouteRequest;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.MountainRoute;
import com.enigmacamp.repository.MountainRouteRepository;
import com.enigmacamp.service.MountainRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MountainRouteServiceImpl implements MountainRouteService {
    @Autowired
    private MountainRouteRepository mountainRouteRepository;

    @Override
    public MountainRoute create(MountainRouteRequest request) {
        return mountainRouteRepository.save(MountainRoute.builder().build());
    }

    @Override
    public MountainRoute delete(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<MountainRoute> getAllByMountain(Mountain mountain) {
        return mountainRouteRepository.findMountainRouteByMountain(mountain);
    }

    private MountainRoute findByIdOrThrowNotFound(String id) {
        return mountainRouteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MountainRoute with id: " + id + " is not found!"));
    }
}
