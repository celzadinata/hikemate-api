package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.HikerResponse;
import com.enigmacamp.model.dto.response.RangerResponse;
import com.enigmacamp.model.entity.Hiker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;

public interface HikerService {
    HikerResponse create(HikerRequest request);
    Page<HikerResponse> getAllHikers(SearchRequest pageable);
    HikerResponse getById(String id);
    HikerResponse updateHiker(HikerRequest request);
    void delete(String id);
}
