package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.MountainRequest;
import com.enigmacamp.model.dto.response.MountainResponse;

import java.util.List;

public interface MountainService {
    MountainResponse create(MountainRequest request);
    List<MountainResponse> getAll();
    MountainResponse getById(String id);
    MountainResponse update(MountainRequest request);
    MountainResponse delete(String id);
}
