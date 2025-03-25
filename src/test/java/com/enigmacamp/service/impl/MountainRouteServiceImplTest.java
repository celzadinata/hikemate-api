package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.MountainRouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.MountainRoute;
import com.enigmacamp.model.entity.Route;
import com.enigmacamp.repository.MountainRepository;
import com.enigmacamp.repository.MountainRouteRepository;
import com.enigmacamp.repository.RouteRepository;
import com.enigmacamp.utils.specifications.MountainRouteSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MountainRouteServiceImplTest {

    @Mock
    private MountainRouteRepository mockMountainRouteRepository;
    @Mock
    private MountainRepository mockMountainRepository;
    @Mock
    private RouteRepository mockRouteRepository;

    @InjectMocks
    private MountainRouteServiceImpl mountainRouteServiceImplUnderTest;

    @Test
    void testCreate() {
        // Setup
        final MountainRouteRequest request = MountainRouteRequest.builder()
                .mountainId("mountainId")
                .routeId("routeId")
                .build();
        final MountainRoute expectedResult = MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build();
        when(mockMountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(mockRouteRepository.findById("routeId")).thenReturn(Optional.of(Route.builder().build()));

        // Configure MountainRouteRepository.save(...).
        final MountainRoute mountainRoute = MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build();
        when(mockMountainRouteRepository.save(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build())).thenReturn(mountainRoute);

        // Run the test
        final MountainRoute result = mountainRouteServiceImplUnderTest.create(request);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testCreate_MountainRepositoryReturnsAbsent() {
        // Setup
        final MountainRouteRequest request = MountainRouteRequest.builder()
                .mountainId("mountainId")
                .routeId("routeId")
                .build();
        when(mockMountainRepository.findById("mountainId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> mountainRouteServiceImplUnderTest.create(request))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testCreate_RouteRepositoryReturnsAbsent() {
        // Setup
        final MountainRouteRequest request = MountainRouteRequest.builder()
                .mountainId("mountainId")
                .routeId("routeId")
                .build();
        when(mockMountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(mockRouteRepository.findById("routeId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> mountainRouteServiceImplUnderTest.create(request))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testDelete() {
        // Setup
        final MountainRoute expectedResult = MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build();

        // Configure MountainRouteRepository.findById(...).
        final Optional<MountainRoute> mountainRoute = Optional.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());
        when(mockMountainRouteRepository.findById("id")).thenReturn(mountainRoute);

        // Run the test
        final MountainRoute result = mountainRouteServiceImplUnderTest.delete("id");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockMountainRouteRepository).delete(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());
    }

    @Test
    void testDelete_MountainRouteRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockMountainRouteRepository.findById("id")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> mountainRouteServiceImplUnderTest.delete("id"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetAllByMountain() {
        // Setup
        final Mountain mountain = Mountain.builder().build();
        final List<MountainRoute> expectedResult = List.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());

        // Configure MountainRouteRepository.findMountainRouteByMountain(...).
        final List<MountainRoute> mountainRouteList = List.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());
        when(mockMountainRouteRepository.findMountainRouteByMountain(Mountain.builder().build()))
                .thenReturn(mountainRouteList);

        // Run the test
        final List<MountainRoute> result = mountainRouteServiceImplUnderTest.getAllByMountain(mountain);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetAllByMountain_MountainRouteRepositoryReturnsNoItems() {
        // Setup
        final Mountain mountain = Mountain.builder().build();
        when(mockMountainRouteRepository.findMountainRouteByMountain(Mountain.builder().build()))
                .thenReturn(Collections.emptyList());

        // Run the test
        final List<MountainRoute> result = mountainRouteServiceImplUnderTest.getAllByMountain(mountain);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetById() {
        // Setup
        final MountainRoute expectedResult = MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build();

        // Configure MountainRouteRepository.findById(...).
        final Optional<MountainRoute> mountainRoute = Optional.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());
        when(mockMountainRouteRepository.findById("id")).thenReturn(mountainRoute);

        // Run the test
        final MountainRoute result = mountainRouteServiceImplUnderTest.getById("id");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetById_MountainRouteRepositoryReturnsAbsent() {
        // Setup
        when(mockMountainRouteRepository.findById("id")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> mountainRouteServiceImplUnderTest.getById("id"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetAll() {
        // Setup
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build();
        when(mockMountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(mockRouteRepository.findById("routeId")).thenReturn(Optional.of(Route.builder().build()));

        // Configure MountainRouteRepository.findAll(...).
        final Page<MountainRoute> mountainRoutes = new PageImpl<>(List.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build()));
        when(mockMountainRouteRepository.findAll(any(MountainRouteSpecification.class),
                any(Pageable.class))).thenReturn(mountainRoutes);

        // Run the test
        final Page<MountainRoute> result = mountainRouteServiceImplUnderTest.getAll("mountainId", "routeId",
                searchRequest);

        // Verify the results
    }

    @Test
    void testGetAll_MountainRepositoryReturnsAbsent() {
        // Setup
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build();
        when(mockMountainRepository.findById("mountainId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(
                () -> mountainRouteServiceImplUnderTest.getAll("mountainId", "routeId", searchRequest))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetAll_RouteRepositoryReturnsAbsent() {
        // Setup
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build();
        when(mockMountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(mockRouteRepository.findById("routeId")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(
                () -> mountainRouteServiceImplUnderTest.getAll("mountainId", "routeId", searchRequest))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetAll_MountainRouteRepositoryReturnsNoItems() {
        // Setup
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build();
        when(mockMountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(mockRouteRepository.findById("routeId")).thenReturn(Optional.of(Route.builder().build()));
        when(mockMountainRouteRepository.findAll(any(MountainRouteSpecification.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Run the test
        final Page<MountainRoute> result = mountainRouteServiceImplUnderTest.getAll("mountainId", "routeId",
                searchRequest);

        // Verify the results
    }
}
