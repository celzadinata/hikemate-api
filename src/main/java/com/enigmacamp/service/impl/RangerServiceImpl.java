package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.response.RangerResponse;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.repository.RangerRepository;
import com.enigmacamp.service.RangerService;
import com.enigmacamp.utils.mapper.RangerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class RangerServiceImpl implements RangerService {

    @Autowired
    private RangerRepository rangerRepository;

    @Autowired
    private RangerMapper rangerMapper;

    @Override
    public RangerResponse create(RangerRequest request) {
        Timestamp currentTimeStamp = new Timestamp(new Date().getTime());
        Ranger newRanger = rangerMapper.requestToEntity(request);
        if (request.getUserAccount() != null) {
            newRanger.setUserAccount(request.getUserAccount());
        }
        newRanger.setAssignedAt(currentTimeStamp);
        newRanger.setCreatedAt(currentTimeStamp);
        newRanger.setUpdatedAt(currentTimeStamp);

        return rangerMapper.entityToResponse(rangerRepository.save(newRanger));
    }

    @Override
    public Ranger getByIdEntity(String id) {
        return rangerRepository.findById(id).orElseThrow(() -> new RuntimeException("Ranger not found"));
    }

    @Override
    public Ranger getByUserAccountEntity(UserAccount userAccount) {
        return rangerRepository.findRangerByUserAccount(userAccount).orElseThrow(() -> new RuntimeException("Ranger not found"));
    }
}
