package com.enigmacamp.service.impl;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.constant.enums.MountainStatus;
import com.enigmacamp.model.dto.request.MountainRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.MountainResponse;
import com.enigmacamp.model.dto.response.RangerResponse;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        if (request.getBaseCampImages() != null) {
            List<Image> baseCampImages = new ArrayList<>();
            request.getBaseCampImages().forEach(item -> {
                Image image = imageService.create(item, Tables.MOUNTAINS + "/basecamp");
                baseCampImages.add(image);
            });
            newMountain.setBaseCampImages(baseCampImages);
            System.out.println(newMountain.getBaseCampImages());
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

        return mountainPage.map(item -> {
            RangerResponse rangerResponse = rangerService.getByMountainId(item);
            MountainResponse mountainResponse =  mountainMapper.entityToResponse(item);
            mountainResponse.setRangerResponse(rangerResponse);
            return mountainResponse;
        });
    }

    @Override
    public MountainResponse getById(String id) {
        Mountain mountain = findByIdOrThrowNotFound(id);
        return mountainMapper.entityToResponse(mountain);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MountainResponse update(MountainRequest request) {
        Mountain mountain = findByIdOrThrowNotFound(request.getId());
        Timestamp currentTimeStamp = new Timestamp(new Date().getTime());

        mountain.setName(request.getName());
        mountain.setLocation(request.getLocation());
        mountain.setStatus(MountainStatus.valueOf(request.getStatus()));
        mountain.setPrice(request.getPrice());
        mountain.setDescription(request.getDescription());
        mountain.setToilet(request.getToilet());
        mountain.setWater(request.getWater());
        mountain.setQuotaLimit(request.getQuotaLimit());
        mountain.setIsOpen(request.getIsOpen());
        mountain.setUpdatedAt(currentTimeStamp);

        if (request.getAssignedRanger() != null && !request.getAssignedRanger().getUserId().isEmpty()) {
            Ranger oldRanger = rangerService.getByMountainIdEntity(mountain);
            oldRanger.setMountain(null);
            Ranger newRanger = rangerService.getByIdEntity(request.getAssignedRanger().getUserId());
            newRanger.setMountain(mountain);
        }

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            if (mountain.getImage() != null) {
                imageService.removeImageFromCloudinary(mountain.getImage().getPath());
                imageService.deleteById(mountain.getImage().getId());
            }

            Image newImage = imageService.create(request.getImage(), Tables.MOUNTAINS);
            mountain.setImage(newImage);
        }

        if (request.getBaseCampImages() != null && !request.getBaseCampImages().isEmpty()) {
            List<Image> imagesToDelete = new ArrayList<>();
            for (Image baseCampImage : mountain.getBaseCampImages()) {
                Image image = imageService.getImage(baseCampImage.getId());
                imagesToDelete.add(image);
                imageService.removeImageFromCloudinary(image.getPath());
                imageService.deleteById(image.getId());
            }
            mountain.getBaseCampImages().removeAll(imagesToDelete);

            List<Image> newImages = new ArrayList<>();
            for (MultipartFile baseCampImage : request.getBaseCampImages()) {
                Image newImage = imageService.create(baseCampImage, Tables.MOUNTAINS + "/basecamp");
                newImages.add(newImage);
            }
            mountain.getBaseCampImages().addAll(newImages);
        }

        mountainRepository.saveAndFlush(mountain);

        return mountainMapper.entityToResponse(mountain);
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
