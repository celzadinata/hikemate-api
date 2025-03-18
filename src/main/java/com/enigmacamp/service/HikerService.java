package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.response.HikerResponse;

public interface HikerService {
    HikerResponse create(HikerRequest request);
}
