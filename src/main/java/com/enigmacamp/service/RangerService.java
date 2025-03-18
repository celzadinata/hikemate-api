package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.response.RangerResponse;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.UserAccount;

public interface RangerService {
    RangerResponse create(RangerRequest request);

    Ranger getByIdEntity(String id);
    Ranger getByUserAccountEntity(UserAccount userAccount);
}
