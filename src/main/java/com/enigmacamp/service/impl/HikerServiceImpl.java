package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.response.HikerResponse;
import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.repository.HikerRepository;
import com.enigmacamp.service.HikerService;
import com.enigmacamp.utils.mapper.HikerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class HikerServiceImpl implements HikerService {

    @Autowired
    private HikerRepository hikerRepository;

    @Autowired
    private HikerMapper hikerMapper;

    @Override
    public HikerResponse create(HikerRequest request) {
        Timestamp currentTimeStamp = new Timestamp(new Date().getTime());
        Hiker newHiker = hikerMapper.requestToEntity(request);
        if (request.getUserAccount() != null) {
            newHiker.setUserAccount(request.getUserAccount());
        }
        newHiker.setCreatedAt(currentTimeStamp);
        newHiker.setUpdatedAt(currentTimeStamp);

        return hikerMapper.entityToResponse(hikerRepository.save(newHiker));
    }

    @Override
    public Hiker getByIdEntity(String id) {
        return hikerRepository.findById(id).orElseThrow(() -> new RuntimeException("Hiker not found"));
    }
}
