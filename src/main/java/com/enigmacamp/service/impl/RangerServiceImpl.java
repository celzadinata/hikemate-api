package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.RangerResponse;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.repository.RangerRepository;
import com.enigmacamp.service.RangerService;
import com.enigmacamp.utils.exception.ResourceNotFoundException;
import com.enigmacamp.utils.mapper.RangerMapper;
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
    public Page<RangerResponse> getAllRangers(SearchRequest pageable) {
        Sort.Direction direction = Sort.Direction.fromString(pageable.getDirection());
        String fieldName = SortingUtil.sortByValidation(Ranger.class, pageable.getSortBy(), "name");
        Pageable page = PageRequest.of(
                Math.max(pageable.getPage() - 1, 0),
                pageable.getSize(),
                direction,
                fieldName
        );
        Specification<Ranger> specification = hitAllSpecification(pageable.getQuery(), fieldName);
        Page<Ranger> rangers;

        if (specification != null) {
            rangers = rangerRepository.findAll(specification, page);
        } else {
            rangers = rangerRepository.findAll(page);
        }
        return rangers.map(rangerMapper::entityToResponse);
    }

    @Override
    public RangerResponse getById(String id) {
        Ranger ranger = rangerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ranger not found", new RuntimeException("Ranger not found")));
        return rangerMapper.entityToResponse(ranger);
    }

    @Override
    public RangerResponse updateRanger(RangerRequest request) {
        Ranger existingRanger = findByIdOrThrowNotFound(request.getUserAccount().getId());
        Ranger updatedRanger = Ranger.builder()
                .id(existingRanger.getId())
                .name(request.getName() != null ? request.getName() : existingRanger.getName())
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : existingRanger.getPhoneNumber())
                .assignedAt(existingRanger.getAssignedAt())
                .userAccount(request.getUserAccount() != null ? request.getUserAccount() : existingRanger.getUserAccount())
                .assignedAt(new Timestamp(new Date().getTime()))
                .build();
        return rangerMapper.entityToResponse(rangerRepository.save(updatedRanger));
    }

    @Override
    public void delete(String id) {
        Ranger ranger = rangerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ranger not found", new RuntimeException("Ranger not found")));
        rangerRepository.delete(ranger);
    }

    private Specification<Ranger> hitAllSpecification(String request, String fieldName) {
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
    public Ranger getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public Ranger getByUserAccountEntity(UserAccount userAccount) {
        return rangerRepository.findRangerByUserAccount(userAccount).orElseThrow(() -> new RuntimeException("Ranger not found"));
    }

    @Override
    public RangerResponse getByMountainId(Mountain mountain) {
        Ranger ranger = rangerRepository.findByMountain(mountain)
                .orElseThrow(() -> new RuntimeException("No ranger found for this mountain!"));

        return rangerMapper.entityToResponse(ranger);
    }

    @Override
    public Ranger getByMountainIdEntity(Mountain mountain) {
        return rangerRepository.findByMountain(mountain)
                .orElseThrow(() -> new RuntimeException("No ranger found for this mountain!"));
    }

    private Ranger findByIdOrThrowNotFound(String id){
        return rangerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ranger Not Found", new RuntimeException("Ranger ga ketemu"))
        );
    }
}

