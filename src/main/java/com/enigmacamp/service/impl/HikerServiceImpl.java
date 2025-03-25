package com.enigmacamp.service.impl;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.HikerResponse;
import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.model.entity.Image;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.repository.HikerRepository;
import com.enigmacamp.service.HikerService;
import com.enigmacamp.service.ImageService;
import com.enigmacamp.utils.exception.ResourceNotFoundException;
import com.enigmacamp.utils.mapper.HikerMapper;
import com.enigmacamp.utils.SortingUtil;
import com.enigmacamp.utils.validate_request.HikerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class HikerServiceImpl implements HikerService {

    @Autowired
    private HikerRepository hikerRepository;

    @Autowired
    private HikerMapper hikerMapper;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HikerValidation hikerValidation;

    private final Timestamp currentTimeStamp = new Timestamp(new Date().getTime());

    @Transactional(rollbackFor = Exception.class)
    @Override
    public HikerResponse create(HikerRequest request) {
        Hiker newHiker = hikerMapper.requestToEntity(request);
        if (request.getUserAccount() != null) {
            newHiker.setUserAccount(request.getUserAccount());
        }
        handleImages(request, newHiker, "create");

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public HikerResponse updateHiker(HikerRequest request) {
        hikerValidation.validateUpdateRequest(request);
        Hiker existingHiker = findByIdOrThrowNotFound(request.getId());
        updateHikerFields(request, existingHiker);
        handleImages(request, existingHiker, "update");

        hikerRepository.saveAndFlush(existingHiker);
        return hikerMapper.entityToResponse(existingHiker);
    }

    @Override
    public HikerResponse delete(String id) {
        Hiker hiker = findByIdOrThrowNotFound(id);
        hiker.setDeletedAt(currentTimeStamp);
        hikerRepository.saveAndFlush(hiker);
        return hikerMapper.entityToResponse(hiker);
    }

    private Specification<Hiker> hitAllSpecification(String request, String fieldName) {
        Specification<Hiker> specification = (root, query, cb) -> cb.isNull(root.get("deletedAt"));

        if (request != null && !request.isEmpty()) {
            if ("name".equals(fieldName)) {
                Specification<Hiker> nameSpecification = (root, query, cb) -> cb.like(root.get("name"), "%" + request + "%");
                specification = specification.and(nameSpecification);
            } else {
                Specification<Hiker> codeSpecification = (root, query, cb) -> cb.equal(root.get("code"), request);
                specification = specification.and(codeSpecification);
            }
        }

        return specification;
    }

    @Override
    public Hiker getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public Hiker getByUserAccountEntity(UserAccount userAccount) {
        return hikerRepository.findHikerByUserAccountAndDeletedAtIsNull(userAccount).orElseThrow(() -> new RuntimeException("Hiker Not Found!"));
    }

    private Hiker findByIdOrThrowNotFound(String id){
        return hikerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Hiker Not Found", new RuntimeException("Hiker ga ketemu"))
        );
    }

    private void updateHikerFields(HikerRequest request, Hiker hiker) {
        hiker.setName(request.getName() != null ? request.getName() : hiker.getName());
        hiker.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : hiker.getPhoneNumber());
        hiker.setUpdatedAt(currentTimeStamp);

        if (request.getPassword() != null && !request.getPassword().isEmpty() ) {
            hiker.getUserAccount().setPassword(passwordEncoder.encode(request.getPassword()));
        }
    }

    private void handleImages(HikerRequest request, Hiker hiker, String method) {
        if (request.getKtpImage() != null) {
            Image oldKtp = hiker.getKtp() != null ? hiker.getKtp() : null;
            hiker.setKtp(imageService.create(request.getKtpImage(), Tables.HIKER));
            if (oldKtp != null && method.equals("update")) {
                imageService.removeImageFromCloudinary(oldKtp.getPath());
                imageService.deleteById(oldKtp.getId());
            }
        }

        if (request.getProfilePicture() != null) {
            Image oldProfilePicture = hiker.getProfilePicture() != null ? hiker.getProfilePicture() : null;
            hiker.setProfilePicture(imageService.create(request.getProfilePicture(), Tables.HIKER));
            if (oldProfilePicture != null && method.equals("update")) {
                imageService.removeImageFromCloudinary(oldProfilePicture.getPath());
                imageService.deleteById(oldProfilePicture.getId());
            }
        }
    }
}

