package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.MountainRouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.MountainRoute;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MountainRouteService {
    MountainRoute create(MountainRouteRequest request);
    MountainRoute delete(String id);
    MountainRoute getById(String id);
    Page<MountainRoute> getAll(String mountainId, String routeId, SearchRequest searchRequest);
    List<MountainRoute> getAllByMountain(Mountain mountain);
}
