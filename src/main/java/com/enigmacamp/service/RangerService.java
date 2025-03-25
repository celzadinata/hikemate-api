package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.RangerResponse;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;

import java.util.List;

public interface RangerService {
    RangerResponse create(RangerRequest request);
    Page<RangerResponse> getAllRangers(SearchRequest pageable);
    RangerResponse getById(String id);
    RangerResponse updateRanger(RangerRequest request);
    RangerResponse delete(String id);
    Ranger getByIdEntity(String id);
    Ranger getByUserAccountEntity(UserAccount userAccount);
    RangerResponse getByMountainId(Mountain mountain);
    Ranger getByMountainIdEntity(Mountain mountain);
}
