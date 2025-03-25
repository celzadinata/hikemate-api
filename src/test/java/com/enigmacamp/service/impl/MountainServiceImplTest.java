package com.enigmacamp.service.impl;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.constant.enums.MountainStatus;
import com.enigmacamp.model.dto.request.*;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MountainServiceImplTest {

    @Mock
    private MountainRepository mountainRepository;
    @Mock
    private MountainMapper mountainMapper;
    @Mock
    private RangerMapper rangerMapper;
    @Mock
    private MountainRouteMapper mountainRouteMapper;
    @Mock
    private AuthService authService;
    @Mock
    private RangerService rangerService;
    @Mock
    private ImageService imageService;
    @Mock
    private RouteService routeService;
    @Mock
    private UserService userService;
    @Mock
    private MountainRouteService mountainRouteService;
    @Mock
    private MountainValidation mountainValidation;

    private MountainServiceImpl mountainService;

    @BeforeEach
    void setUp() {
        mountainService = new MountainServiceImpl(mountainRepository, mountainMapper,
                rangerMapper, mountainRouteMapper, authService, rangerService, imageService,
                routeService, userService, mountainRouteService, mountainValidation);
    }

    @Test
    void testCreate() {
        final MountainRequest request = MountainRequest.builder()
                .id("id")
                .name("name")
                .location("location")
                .status("status")
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .assignedRanger(NewUserRequest.builder()
                        .userId("userId")
                        .build())
                .image(new MockMultipartFile("name", "content".getBytes()))
                .baseCampImages(List.of(new MockMultipartFile("name", "content".getBytes())))
                .mountainRoutes(List.of(RouteRequest.builder()
                        .id("id")
                        .build()))
                .build();
        final MountainResponse expectedResult = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();

        final Mountain mountain = Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build();
        when(mountainMapper.requestToEntity(MountainRequest.builder()
                .id("id")
                .name("name")
                .location("location")
                .status("status")
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .assignedRanger(NewUserRequest.builder()
                        .userId("userId")
                        .build())
                .image(new MockMultipartFile("name", "content".getBytes()))
                .baseCampImages(List.of(new MockMultipartFile("name", "content".getBytes())))
                .mountainRoutes(List.of(RouteRequest.builder()
                        .id("id")
                        .build()))
                .build())).thenReturn(mountain);

        final Image image = Image.builder()
                .id("id")
                .path("path")
                .build();
        when(imageService.create(any(MultipartFile.class), eq(Tables.MOUNTAINS))).thenReturn(image);

        final Mountain mountain1 = Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build();
        when(mountainRepository.save(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(mountain1);

        final RegisterResponse registerResponse = RegisterResponse.builder()
                .userId("userId")
                .build();
        when(authService.registerRanger(NewUserRequest.builder()
                .userId("userId")
                .build())).thenReturn(registerResponse);

        when(userService.loadUserById("userId")).thenReturn(UserAccount.builder().build());

        final Ranger ranger = Ranger.builder()
                .mountain(Mountain.builder()
                        .name("name")
                        .location("location")
                        .status(MountainStatus.DANGEROUS)
                        .price(new BigDecimal(12000))
                        .description("description")
                        .toilet(false)
                        .water("water")
                        .quotaLimit(10)
                        .isOpen(false)
                        .image(Image.builder()
                                .id("id")
                                .path("path")
                                .build())
                        .baseCampImages(List.of(Image.builder()
                                .id("id")
                                .path("path")
                                .build()))
                        .build())
                .build();
        when(rangerService.getByUserAccountEntity(UserAccount.builder().build())).thenReturn(ranger);

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mountainMapper.entityToResponse(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(mountainResponse);

        when(rangerMapper.entityToResponse(Ranger.builder()
                .mountain(Mountain.builder()
                        .name("name")
                        .location("location")
                        .status(MountainStatus.DANGEROUS)
                        .price(new BigDecimal(12000))
                        .description("description")
                        .toilet(false)
                        .water("water")
                        .quotaLimit(10)
                        .isOpen(false)
                        .image(Image.builder()
                                .id("id")
                                .path("path")
                                .build())
                        .baseCampImages(List.of(Image.builder()
                                .id("id")
                                .path("path")
                                .build()))
                        .build())
                .build())).thenReturn(RangerResponse.builder().build());
        when(routeService.getByIdEntity("id")).thenReturn(Route.builder().build());
        when(mountainRouteService.create(MountainRouteRequest.builder().build()))
                .thenReturn(MountainRoute.builder().build());
        when(mountainRouteMapper.entityToResponse(MountainRoute.builder().build()))
                .thenReturn(MountainRouteResponse.builder().build());
        final MountainResponse result = mountainService.create(request);
        assertThat(result).isEqualTo(expectedResult);
        verify(mountainValidation).validateCreateRequest(MountainRequest.builder()
                .id("id")
                .name("name")
                .location("location")
                .status("status")
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .assignedRanger(NewUserRequest.builder()
                        .userId("userId")
                        .build())
                .image(new MockMultipartFile("name", "content".getBytes()))
                .baseCampImages(List.of(new MockMultipartFile("name", "content".getBytes())))
                .mountainRoutes(List.of(RouteRequest.builder()
                        .id("id")
                        .build()))
                .build());
        verify(imageService).removeImageFromCloudinary("path");
        verify(imageService).deleteById("id");
    }

    @Test
    void testGetAll() {
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(1)
                .size(10)
                .direction("desc")
                .sortBy("sortBy")
                .build();

        final Page<Mountain> mountains = new PageImpl<>(List.of(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build()));
        when(mountainRepository.findAll(any(MountainSpecification.class), any(Pageable.class)))
                .thenReturn(mountains);

        when(rangerService.getByMountainId(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(RangerResponse.builder().build());
        when(mountainRouteService.getAllByMountain(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(List.of(MountainRoute.builder().build()));
        when(mountainRouteMapper.entityToResponse(MountainRoute.builder().build()))
                .thenReturn(MountainRouteResponse.builder().build());

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mountainMapper.entityToResponse(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(mountainResponse);
        final Page<MountainResponse> result = mountainService.getAll("name", "startPrice", "endPrice",
                "status", "location", searchRequest);
    }


    @Test
    void testGetAllMountainRouteServiceReturnsNoItems() {
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(1)
                .size(10)
                .direction("desc")
                .sortBy("sortBy")
                .build();

        final Page<Mountain> mountains = new PageImpl<>(List.of(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build()));
        when(mountainRepository.findAll(any(MountainSpecification.class), any(Pageable.class)))
                .thenReturn(mountains);

        when(rangerService.getByMountainId(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(RangerResponse.builder().build());
        when(mountainRouteService.getAllByMountain(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(Collections.emptyList());

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mountainMapper.entityToResponse(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(mountainResponse);
        final Page<MountainResponse> result = mountainService.getAll("name", "startPrice", "endPrice",
                "status", "location", searchRequest);
    }

    @Test
    void testGetById() {
        final MountainResponse expectedResult = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();

        final Optional<Mountain> mountain = Optional.of(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build());
        when(mountainRepository.findById("id")).thenReturn(mountain);

        when(mountainRouteService.getAllByMountain(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(List.of(MountainRoute.builder().build()));
        when(mountainRouteMapper.entityToResponse(MountainRoute.builder().build()))
                .thenReturn(MountainRouteResponse.builder().build());

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mountainMapper.entityToResponse(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(mountainResponse);
        final MountainResponse result = mountainService.getById("id");
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetByIdMountainRepositoryReturnsAbsent() {
        when(mountainRepository.findById("id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> mountainService.getById("id"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testUpdate() {
        final MountainRequest request = MountainRequest.builder()
                .id("id")
                .name("name")
                .location("location")
                .status("status")
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .assignedRanger(NewUserRequest.builder()
                        .userId("userId")
                        .build())
                .image(new MockMultipartFile("name", "content".getBytes()))
                .baseCampImages(List.of(new MockMultipartFile("name", "content".getBytes())))
                .mountainRoutes(List.of(RouteRequest.builder()
                        .id("id")
                        .build()))
                .build();
        final MountainResponse expectedResult = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();

        final Optional<Mountain> mountain = Optional.of(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build());
        when(mountainRepository.findById("id")).thenReturn(mountain);

        // Configure RangerService.getByMountainIdEntity(...).
        final Ranger ranger = Ranger.builder()
                .mountain(Mountain.builder()
                        .name("name")
                        .location("location")
                        .status(MountainStatus.DANGEROUS)
                        .price(new BigDecimal(12000))
                        .description("description")
                        .toilet(false)
                        .water("water")
                        .quotaLimit(10)
                        .isOpen(false)
                        .image(Image.builder()
                                .id("id")
                                .path("path")
                                .build())
                        .baseCampImages(List.of(Image.builder()
                                .id("id")
                                .path("path")
                                .build()))
                        .build())
                .build();
        when(rangerService.getByMountainIdEntity(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(ranger);

        // Configure RangerService.getByIdEntity(...).
        final Ranger ranger1 = Ranger.builder()
                .mountain(Mountain.builder()
                        .name("name")
                        .location("location")
                        .status(MountainStatus.DANGEROUS)
                        .price(new BigDecimal(12000))
                        .description("description")
                        .toilet(false)
                        .water("water")
                        .quotaLimit(10)
                        .isOpen(false)
                        .image(Image.builder()
                                .id("id")
                                .path("path")
                                .build())
                        .baseCampImages(List.of(Image.builder()
                                .id("id")
                                .path("path")
                                .build()))
                        .build())
                .build();
        when(rangerService.getByIdEntity("userId")).thenReturn(ranger1);

        final Image image = Image.builder()
                .id("id")
                .path("path")
                .build();
        when(imageService.create(any(MultipartFile.class), eq(Tables.MOUNTAINS))).thenReturn(image);

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mountainMapper.entityToResponse(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(mountainResponse);
        final MountainResponse result = mountainService.update(request);
        assertThat(result).isEqualTo(expectedResult);
        verify(mountainValidation).validateUpdateRequest(MountainRequest.builder()
                .id("id")
                .name("name")
                .location("location")
                .status("status")
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .assignedRanger(NewUserRequest.builder()
                        .userId("userId")
                        .build())
                .image(new MockMultipartFile("name", "content".getBytes()))
                .baseCampImages(List.of(new MockMultipartFile("name", "content".getBytes())))
                .mountainRoutes(List.of(RouteRequest.builder()
                        .id("id")
                        .build()))
                .build());
        verify(imageService).removeImageFromCloudinary("path");
        verify(imageService).deleteById("id");
        verify(mountainRepository).saveAndFlush(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build());
    }

    @Test
    void testDelete() {
        final MountainResponse expectedResult = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();

        final Optional<Mountain> mountain = Optional.of(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build());
        when(mountainRepository.findById("id")).thenReturn(mountain);

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mountainMapper.entityToResponse(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build())).thenReturn(mountainResponse);
        final MountainResponse result = mountainService.delete("id");
        assertThat(result).isEqualTo(expectedResult);
        verify(mountainRepository).saveAndFlush(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build());
    }

    @Test
    void testDeleteMountainRepositoryFindByIdReturnsAbsent() {
        when(mountainRepository.findById("id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> mountainService.delete("id")).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetByIdEntity() {
        final Mountain expectedResult = Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build();

        final Optional<Mountain> mountain = Optional.of(Mountain.builder()
                .name("name")
                .location("location")
                .status(MountainStatus.DANGEROUS)
                .price(new BigDecimal(12000))
                .description("description")
                .toilet(false)
                .water("water")
                .quotaLimit(10)
                .isOpen(false)
                .image(Image.builder()
                        .id("id")
                        .path("path")
                        .build())
                .baseCampImages(List.of(Image.builder()
                        .id("id")
                        .path("path")
                        .build()))
                .build());
        when(mountainRepository.findById("id")).thenReturn(mountain);
        final Mountain result = mountainService.getByIdEntity("id");
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetByIdEntityMountainRepositoryReturnsAbsent() {
        when(mountainRepository.findById("id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> mountainService.getByIdEntity("id"))
                .isInstanceOf(ResponseStatusException.class);
    }
}
