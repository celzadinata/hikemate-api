package com.enigmacamp.service.impl;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.model.dto.request.MountainRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.MountainResponse;
import com.enigmacamp.model.dto.response.RegisterResponse;
import com.enigmacamp.model.entity.Image;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.repository.MountainRepository;
import com.enigmacamp.service.*;
import com.enigmacamp.utils.mapper.MountainMapper;
import com.enigmacamp.utils.mapper.RangerMapper;
import com.enigmacamp.utils.specifications.MountainSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class MountainServiceImpl implements MountainService {

    @Autowired
    private MountainRepository mountainRepository;

    @Autowired
    private MountainMapper mountainMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private RangerService rangerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private RangerMapper rangerMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MountainResponse create(MountainRequest request) {;
        Timestamp currentTimeStamp = new Timestamp(new Date().getTime());
        Mountain newMountain = mountainMapper.requestToEntity(request);
        newMountain.setCreatedAt(currentTimeStamp);
        newMountain.setUpdatedAt(currentTimeStamp);

        if (request.getImage() != null) {
            Image image = imageService.create(request.getImage(), Tables.MOUNTAINS);
            newMountain.setImage(image);
        }
        Mountain mountain = mountainRepository.save(newMountain);

        RegisterResponse newRangerAccount = authService.registerRanger(request.getAssignedRanger());

        UserAccount userAccount = userService.loadUserById(newRangerAccount.getUserId());
        Ranger ranger = rangerService.getByUserAccountEntity(userAccount);
        ranger.setMountain(mountain);

        MountainResponse mountainResponse = mountainMapper.entityToResponse(mountain);
        mountainResponse.setRangerResponse(rangerMapper.entityToResponse(ranger));

        return mountainResponse;
    }

    @Override
    public Page<MountainResponse> getAll(String name, String startPrice, String endPrice, String status, String location, SearchRequest searchRequest) {
        MountainSpecification specification = new MountainSpecification(name, startPrice, endPrice, status, location);
        Sort.Direction sortDirection = searchRequest.getDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sortDirection, searchRequest.getSortBy());
        Page<Mountain> mountainPage = mountainRepository.findAll(specification, pageable);

        return mountainPage.map(mountainMapper::entityToResponse);
    }

    @Override
    public MountainResponse getById(String id) {
        Mountain mountain =findByIdOrThrowNotFound(id);
        return mountainMapper.entityToResponse(mountain);
    }

    @Override
    public MountainResponse update(MountainRequest request) {
        return null;
    }

    @Override
    public MountainResponse delete(String id) {
        Mountain mountain = findByIdOrThrowNotFound(id);
        mountain.setDeletedAt(new Timestamp(new Date().getTime()));
        mountainRepository.saveAndFlush(mountain);
        return mountainMapper.entityToResponse(mountain);
    }

    @Override
    public Mountain getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    private Mountain findByIdOrThrowNotFound(String id){
        return mountainRepository.findById(id).orElseThrow(() -> new RuntimeException("Mountain not found!", new RuntimeException("Mountain not found!", new Throwable())));
    }
}
