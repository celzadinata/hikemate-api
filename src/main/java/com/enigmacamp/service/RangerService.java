package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.RangerResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RangerService {
    RangerResponse create(RangerRequest request);
    Page<RangerResponse> getAllRangers(SearchRequest pageable);
    RangerResponse getById(String id);
    RangerResponse updateRanger(RangerRequest request);
    void delete(String id);
    Ranger getByIdEntity(String id);
    Ranger getByUserAccountEntity(UserAccount userAccount);
}
