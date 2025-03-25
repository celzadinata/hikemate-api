package com.enigmacamp.service.impl;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.constant.enums.MountainStatus;
import com.enigmacamp.model.dto.request.MountainRequest;
import com.enigmacamp.model.dto.request.MountainRouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.MountainResponse;
import com.enigmacamp.model.dto.response.MountainRouteResponse;
import com.enigmacamp.model.dto.response.RangerResponse;
import com.enigmacamp.model.dto.response.RegisterResponse;
import com.enigmacamp.model.entity.*;
import com.enigmacamp.repository.MountainRepository;
import com.enigmacamp.service.*;
import com.enigmacamp.utils.mapper.MountainMapper;
import com.enigmacamp.utils.mapper.MountainRouteMapper;
import com.enigmacamp.utils.mapper.RangerMapper;
import com.enigmacamp.utils.specifications.MountainSpecification;
import com.enigmacamp.utils.validate_request.MountainValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService {
    private final MountainRepository mountainRepository;
    private final MountainMapper mountainMapper;
    private final RangerMapper rangerMapper;
    private final MountainRouteMapper mountainRouteMapper;
    private final AuthService authService;
    private final RangerService rangerService;
    private final ImageService imageService;
    private final RouteService routeService;
    private final UserService userService;
    private final MountainRouteService mountainRouteService;
    private final MountainValidation mountainValidation;

    private final Timestamp currentTimeStamp = new Timestamp(new Date().getTime());

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MountainResponse create(MountainRequest request) {
        mountainValidation.validateCreateRequest(request);
        Mountain newMountain = mountainMapper.requestToEntity(request);
        newMountain.setCreatedAt(currentTimeStamp);
        newMountain.setUpdatedAt(currentTimeStamp);
        handleImages(request, newMountain, "create");
        return createMountainResponse(request, newMountain);
    }

    @Override
    public Page<MountainResponse> getAll(String name, String startPrice, String endPrice, String status, String location, SearchRequest searchRequest) {
        MountainSpecification specification = new MountainSpecification(name, startPrice, endPrice, status, location);
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.Direction.fromString(searchRequest.getDirection()), searchRequest.getSortBy());
        return mountainRepository.findAll(specification, pageable).map(item -> {
            RangerResponse rangerResponse = rangerService.getByMountainId(item);
            List<MountainRouteResponse> mountainRouteResponseList = mountainRouteService.getAllByMountain(item)
                    .stream()
                    .map(mountainRouteMapper::entityToResponse)
                    .toList();
            MountainResponse mountainResponse = mountainMapper.entityToResponse(item);
            mountainResponse.setRangerResponse(rangerResponse);
            mountainResponse.setMountainRoutes(mountainRouteResponseList);
            System.out.println(mountainResponse);
            return mountainResponse;
        });
    }

    @Override
    public MountainResponse getById(String id) {
        Mountain mountain = findByIdOrThrowNotFound(id);
        List<MountainRouteResponse> mountainRouteResponseList = mountainRouteService.getAllByMountain(mountain)
                .stream()
                .map(mountainRouteMapper::entityToResponse)
                .toList();
        MountainResponse mountainResponse = mountainMapper.entityToResponse(mountain);
        mountainResponse.setMountainRoutes(mountainRouteResponseList);
        return mountainResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MountainResponse update(MountainRequest request) {
        mountainValidation.validateUpdateRequest(request);
        Mountain mountain = findByIdOrThrowNotFound(request.getId());
        updateMountainFields(request, mountain);
        handleImages(request, mountain, "update");
        mountainRepository.saveAndFlush(mountain);
        return mountainMapper.entityToResponse(mountain);
    }

    @Override
    public MountainResponse delete(String id) {
        Mountain mountain = findByIdOrThrowNotFound(id);
        mountain.setDeletedAt(currentTimeStamp);
        mountainRepository.saveAndFlush(mountain);
        return mountainMapper.entityToResponse(mountain);
    }

    @Override
    public Mountain getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    private Mountain findByIdOrThrowNotFound(String id) {
        return mountainRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mountain with id: " + id + " is not found!"));
    }

    private void handleImages(MountainRequest request, Mountain mountain, String method) {
        if (request.getImage() != null) {
            Image oldMountainCover = mountain.getImage() != null ? mountain.getImage() : null;
            mountain.setImage(imageService.create(request.getImage(), Tables.MOUNTAINS));
            if (oldMountainCover != null && method.equals("update")) {
                imageService.removeImageFromCloudinary(oldMountainCover.getPath());
                imageService.deleteById(oldMountainCover.getId());
            }
        }

        if (request.getBaseCampImages() != null) {
            List<Image> oldBaseCampImages = mountain.getBaseCampImages() != null && !mountain.getBaseCampImages().isEmpty() ? mountain.getBaseCampImages() : null;
            mountain.setBaseCampImages(createImageList(request.getBaseCampImages()));
            if (oldBaseCampImages != null && method.equals("update")) {
                oldBaseCampImages.forEach(image -> {
                    imageService.removeImageFromCloudinary(image.getPath());
                    imageService.deleteById(image.getId());
                });
            }
        }
    }

    private List<Image> createImageList(List<MultipartFile> baseCampImages) {
        List<Image> images = new ArrayList<>();
        baseCampImages.forEach(item -> images.add(imageService.create(item, Tables.MOUNTAINS + "/basecamp")));
        return images;
    }

    private void updateMountainFields(MountainRequest request, Mountain mountain) {
        mountain.setName(request.getName() != null ? request.getName() : mountain.getName());
        mountain.setLocation(request.getLocation() != null ? request.getLocation() : mountain.getLocation());
        mountain.setStatus(request.getStatus() != null ? MountainStatus.valueOf(request.getStatus()) : mountain.getStatus());
        mountain.setPrice(request.getPrice() != null ? request.getPrice() : mountain.getPrice());
        mountain.setDescription(request.getDescription() != null ? request.getDescription() : mountain.getDescription());
        mountain.setToilet(request.getToilet() != null ? request.getToilet() : mountain.getToilet());
        mountain.setWater(request.getWater() != null ? request.getWater() : mountain.getWater());
        mountain.setQuotaLimit(request.getQuotaLimit() != null ? request.getQuotaLimit() : mountain.getQuotaLimit());
        mountain.setIsOpen(request.getIsOpen() != null ? request.getIsOpen() : mountain.getIsOpen());
        mountain.setUpdatedAt(currentTimeStamp);

        if (request.getAssignedRanger() != null && !request.getAssignedRanger().getUserId().isEmpty()) {
            Ranger oldRanger = rangerService.getByMountainIdEntity(mountain);
            oldRanger.setMountain(null);
            Ranger newRanger = rangerService.getByIdEntity(request.getAssignedRanger().getUserId());
            newRanger.setMountain(mountain);
        }
    }

    private MountainResponse createMountainResponse(MountainRequest request, Mountain newMountain) {
        Mountain mountain = mountainRepository.save(newMountain);
        RegisterResponse newRangerAccount = authService.registerRanger(request.getAssignedRanger());
        Ranger ranger = rangerService.getByUserAccountEntity(userService.loadUserById(newRangerAccount.getUserId()));
        ranger.setMountain(mountain);

        MountainResponse mountainResponse = mountainMapper.entityToResponse(mountain);
        mountainResponse.setRangerResponse(rangerMapper.entityToResponse(ranger));
        if (request.getMountainRoutes() != null) {
            List<MountainRoute> mountainRouteList = getMountainRoutes(request, newMountain);
            mountainResponse.setMountainRoutes(mapMountainRoutesToResponses(mountainRouteList));
        }
        return mountainResponse;
    }

    private List<MountainRoute> getMountainRoutes(MountainRequest request, Mountain newMountain) {
        List<Route> routes = getRouteList(request);
        return createMountainRoutes(newMountain, routes);
    }

    private List<Route> getRouteList(MountainRequest request) {
        List<Route> routes = new ArrayList<>();
        request.getMountainRoutes().forEach(item -> routes.add(routeService.getByIdEntity(item.getId())));
        return routes;
    }

    private List<MountainRoute> createMountainRoutes(Mountain newMountain, List<Route> routes) {
        List<MountainRoute> mountainRouteList = new ArrayList<>();
        routes.forEach(route -> {
            MountainRoute mountainRoute = mountainRouteService.create(MountainRouteRequest.builder().build());
            mountainRouteList.add(mountainRoute);
        });
        return mountainRouteList;
    }

    private List<MountainRouteResponse> mapMountainRoutesToResponses(List<MountainRoute> mountainRouteList) {
        return mountainRouteList.stream().map(mountainRouteMapper::entityToResponse).toList();
    }
}
