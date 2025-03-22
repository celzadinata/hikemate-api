package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.MountainRouteRequest;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.MountainRoute;

import java.util.List;

public interface MountainRouteService {
    MountainRoute create(MountainRouteRequest request);
    MountainRoute delete(String id);
    List<MountainRoute> getAllByMountain(Mountain mountain);
}
