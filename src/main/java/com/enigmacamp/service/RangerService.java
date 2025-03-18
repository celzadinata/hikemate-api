package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.response.RangerResponse;

public interface RangerService {
    RangerResponse create(RangerRequest request);
}
