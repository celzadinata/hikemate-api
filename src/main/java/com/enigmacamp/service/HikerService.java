package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.response.HikerResponse;
import com.enigmacamp.model.entity.Hiker;

public interface HikerService {
    HikerResponse create(HikerRequest request);
    Hiker getByIdEntity(String id);
}
