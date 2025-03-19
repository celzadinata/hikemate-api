package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.HikerResponse;
import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.repository.HikerRepository;
import com.enigmacamp.service.HikerService;
import com.enigmacamp.utils.exception.ResourceNotFoundException;
import com.enigmacamp.utils.mapper.HikerMapper;
import com.enigmacamp.utils.mapper.SortingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<HikerResponse> getAllHikers(SearchRequest pageable) {
        Sort.Direction direction = Sort.Direction.fromString(pageable.getDirection());
        String FieldName = SortingUtil.sortByValidation(Hiker.class, pageable.getSortBy(), "name");
        Pageable page = PageRequest.of(
                Math.max(pageable.getPage() - 1, 0),
                pageable.getSize(),
                direction,
                FieldName
        );
        Specification<Hiker> specification = hitAllSpecification(pageable.getQuery(), FieldName);
        Page<Hiker> hikers;
        if (specification != null) {
            hikers = hikerRepository.findAll(specification, page);
        } else {
            hikers = hikerRepository.findAll(page);
        }
        return hikers.map(hikerMapper::entityToResponse);
    }

    @Override
    public HikerResponse getById(String id) {
        Hiker hiker = hikerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hiker not found", new RuntimeException("Hiker not found")));
        return hikerMapper.entityToResponse(hiker);
    }

    @Override
    public HikerResponse updateHiker(HikerRequest request) {
        Hiker existingHiker = findByIdOrThrowNotFound(request.getUserAccount().getId());
        Hiker updateHiker = Hiker.builder()
                .id(existingHiker.getId())
                .name(request.getName() != null ? request.getName() : existingHiker.getName())
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : existingHiker.getPhoneNumber())
                .userAccount(existingHiker.getUserAccount())
                .build();
        return hikerMapper.entityToResponse(hikerRepository.save(updateHiker));
    }

    @Override
    public void delete(String id) {
        Hiker hiker = hikerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hiker not found", new RuntimeException("Hiker not found")));
        hikerRepository.delete(hiker);
    }

    private Specification<Hiker> hitAllSpecification(String request, String fieldName) {
        if (request != null && !request.isEmpty()) {
            if ("name".equals(fieldName)) {
                return (root, query, cb) -> cb.like(root.get("name"), "%" + request + "%");
            } else {
                return (root, query, cb) -> cb.equal(root.get("code"), request);
            }
        }
        return null;
    }

    @Override
    public Hiker getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    private Hiker findByIdOrThrowNotFound(String id){
        return hikerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("customer Not Found", new RuntimeException("customer ga ketemu"))
        );
    }
}
