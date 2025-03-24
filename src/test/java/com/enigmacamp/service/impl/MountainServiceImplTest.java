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
    private MountainRepository mockMountainRepository;
    @Mock
    private MountainMapper mockMountainMapper;
    @Mock
    private RangerMapper mockRangerMapper;
    @Mock
    private MountainRouteMapper mockMountainRouteMapper;
    @Mock
    private AuthService mockAuthService;
    @Mock
    private RangerService mockRangerService;
    @Mock
    private ImageService mockImageService;
    @Mock
    private RouteService mockRouteService;
    @Mock
    private UserService mockUserService;
    @Mock
    private MountainRouteService mockMountainRouteService;
    @Mock
    private MountainValidation mockMountainValidation;

    private MountainServiceImpl mountainServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        mountainServiceImplUnderTest = new MountainServiceImpl(mockMountainRepository, mockMountainMapper,
                mockRangerMapper, mockMountainRouteMapper, mockAuthService, mockRangerService, mockImageService,
                mockRouteService, mockUserService, mockMountainRouteService, mockMountainValidation);
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

        // Configure MountainMapper.requestToEntity(...).
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
        when(mockMountainMapper.requestToEntity(MountainRequest.builder()
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

        // Configure ImageService.create(...).
        final Image image = Image.builder()
                .id("id")
                .path("path")
                .build();
        when(mockImageService.create(any(MultipartFile.class), eq(Tables.MOUNTAINS))).thenReturn(image);

        // Configure MountainRepository.save(...).
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
        when(mockMountainRepository.save(Mountain.builder()
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

        // Configure AuthService.registerRanger(...).
        final RegisterResponse registerResponse = RegisterResponse.builder()
                .userId("userId")
                .build();
        when(mockAuthService.registerRanger(NewUserRequest.builder()
                .userId("userId")
                .build())).thenReturn(registerResponse);

        when(mockUserService.loadUserById("userId")).thenReturn(UserAccount.builder().build());

        // Configure RangerService.getByUserAccountEntity(...).
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
        when(mockRangerService.getByUserAccountEntity(UserAccount.builder().build())).thenReturn(ranger);

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mockMountainMapper.entityToResponse(Mountain.builder()
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

        when(mockRangerMapper.entityToResponse(Ranger.builder()
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
        when(mockRouteService.getByIdEntity("id")).thenReturn(Route.builder().build());
        when(mockMountainRouteService.create(MountainRouteRequest.builder().build()))
                .thenReturn(MountainRoute.builder().build());
        when(mockMountainRouteMapper.entityToResponse(MountainRoute.builder().build()))
                .thenReturn(MountainRouteResponse.builder().build());
        final MountainResponse result = mountainServiceImplUnderTest.create(request);
        assertThat(result).isEqualTo(expectedResult);
        verify(mockMountainValidation).validateCreateRequest(MountainRequest.builder()
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
        verify(mockImageService).removeImageFromCloudinary("path");
        verify(mockImageService).deleteById("id");
    }

    @Test
    void testGetAll() {
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(1)
                .size(10)
                .direction("desc")
                .sortBy("sortBy")
                .build();

        // Configure MountainRepository.findAll(...).
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
        when(mockMountainRepository.findAll(any(MountainSpecification.class), any(Pageable.class)))
                .thenReturn(mountains);

        when(mockRangerService.getByMountainId(Mountain.builder()
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
        when(mockMountainRouteService.getAllByMountain(Mountain.builder()
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
        when(mockMountainRouteMapper.entityToResponse(MountainRoute.builder().build()))
                .thenReturn(MountainRouteResponse.builder().build());

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mockMountainMapper.entityToResponse(Mountain.builder()
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
        final Page<MountainResponse> result = mountainServiceImplUnderTest.getAll("name", "startPrice", "endPrice",
                "status", "location", searchRequest);
    }


    @Test
    void testGetAll_MountainRouteServiceReturnsNoItems() {
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(1)
                .size(10)
                .direction("desc")
                .sortBy("sortBy")
                .build();

        // Configure MountainRepository.findAll(...).
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
        when(mockMountainRepository.findAll(any(MountainSpecification.class), any(Pageable.class)))
                .thenReturn(mountains);

        when(mockRangerService.getByMountainId(Mountain.builder()
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
        when(mockMountainRouteService.getAllByMountain(Mountain.builder()
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
        when(mockMountainMapper.entityToResponse(Mountain.builder()
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
        final Page<MountainResponse> result = mountainServiceImplUnderTest.getAll("name", "startPrice", "endPrice",
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
        when(mockMountainRepository.findById("id")).thenReturn(mountain);

        when(mockMountainRouteService.getAllByMountain(Mountain.builder()
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
        when(mockMountainRouteMapper.entityToResponse(MountainRoute.builder().build()))
                .thenReturn(MountainRouteResponse.builder().build());

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mockMountainMapper.entityToResponse(Mountain.builder()
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
        final MountainResponse result = mountainServiceImplUnderTest.getById("id");
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetById_MountainRepositoryReturnsAbsent() {
        when(mockMountainRepository.findById("id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> mountainServiceImplUnderTest.getById("id"))
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
        when(mockMountainRepository.findById("id")).thenReturn(mountain);

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
        when(mockRangerService.getByMountainIdEntity(Mountain.builder()
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
        when(mockRangerService.getByIdEntity("userId")).thenReturn(ranger1);

        final Image image = Image.builder()
                .id("id")
                .path("path")
                .build();
        when(mockImageService.create(any(MultipartFile.class), eq(Tables.MOUNTAINS))).thenReturn(image);

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mockMountainMapper.entityToResponse(Mountain.builder()
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
        final MountainResponse result = mountainServiceImplUnderTest.update(request);
        assertThat(result).isEqualTo(expectedResult);
        verify(mockMountainValidation).validateUpdateRequest(MountainRequest.builder()
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
        verify(mockImageService).removeImageFromCloudinary("path");
        verify(mockImageService).deleteById("id");
        verify(mockMountainRepository).saveAndFlush(Mountain.builder()
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
        when(mockMountainRepository.findById("id")).thenReturn(mountain);

        final MountainResponse mountainResponse = MountainResponse.builder()
                .mountainRoutes(List.of(MountainRouteResponse.builder().build()))
                .rangerResponse(RangerResponse.builder().build())
                .build();
        when(mockMountainMapper.entityToResponse(Mountain.builder()
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
        final MountainResponse result = mountainServiceImplUnderTest.delete("id");
        assertThat(result).isEqualTo(expectedResult);
        verify(mockMountainRepository).saveAndFlush(Mountain.builder()
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
    void testDelete_MountainRepositoryFindByIdReturnsAbsent() {
        when(mockMountainRepository.findById("id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> mountainServiceImplUnderTest.delete("id")).isInstanceOf(ResponseStatusException.class);
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
        when(mockMountainRepository.findById("id")).thenReturn(mountain);
        final Mountain result = mountainServiceImplUnderTest.getByIdEntity("id");
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetByIdEntity_MountainRepositoryReturnsAbsent() {
        when(mockMountainRepository.findById("id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> mountainServiceImplUnderTest.getByIdEntity("id"))
                .isInstanceOf(ResponseStatusException.class);
    }
}
