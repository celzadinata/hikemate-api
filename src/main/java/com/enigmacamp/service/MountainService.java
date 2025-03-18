package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.MountainRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.MountainResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MountainService {
    MountainResponse create(MountainRequest request);
    Page<MountainResponse> getAll(String name, String startPrice, String endPrice, String status, String location, SearchRequest searchRequest);
    MountainResponse getById(String id);
    MountainResponse update(MountainRequest request);
    MountainResponse delete(String id);
}
