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
    private MountainRouteRepository mountainRouteRepository;
    @Mock
    private MountainRepository mountainRepository;
    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private MountainRouteServiceImpl mountainRouteService;

    @Test
    void testCreate() {
        final MountainRouteRequest request = MountainRouteRequest.builder()
                .mountainId("mountainId")
                .routeId("routeId")
                .build();
        final MountainRoute expectedResult = MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build();
        when(mountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(routeRepository.findById("routeId")).thenReturn(Optional.of(Route.builder().build()));

        final MountainRoute mountainRoute = MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build();
        when(mountainRouteRepository.save(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build())).thenReturn(mountainRoute);

        final MountainRoute result = mountainRouteService.create(request);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testCreate_MountainRepositoryReturnsAbsent() {
        final MountainRouteRequest request = MountainRouteRequest.builder()
                .mountainId("mountainId")
                .routeId("routeId")
                .build();
        when(mountainRepository.findById("mountainId")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mountainRouteService.create(request))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testCreate_RouteRepositoryReturnsAbsent() {
        final MountainRouteRequest request = MountainRouteRequest.builder()
                .mountainId("mountainId")
                .routeId("routeId")
                .build();
        when(mountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(routeRepository.findById("routeId")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mountainRouteService.create(request))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testDelete() {
        final MountainRoute expectedResult = MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build();

        final Optional<MountainRoute> mountainRoute = Optional.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());
        when(mountainRouteRepository.findById("id")).thenReturn(mountainRoute);

        final MountainRoute result = mountainRouteService.delete("id");

        assertThat(result).isEqualTo(expectedResult);
        verify(mountainRouteRepository).delete(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());
    }

    @Test
    void testDelete_MountainRouteRepositoryFindByIdReturnsAbsent() {
        when(mountainRouteRepository.findById("id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mountainRouteService.delete("id"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetAllByMountain() {
        final Mountain mountain = Mountain.builder().build();
        final List<MountainRoute> expectedResult = List.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());

        final List<MountainRoute> mountainRouteList = List.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());
        when(mountainRouteRepository.findMountainRouteByMountain(Mountain.builder().build()))
                .thenReturn(mountainRouteList);

        final List<MountainRoute> result = mountainRouteService.getAllByMountain(mountain);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetAllByMountain_MountainRouteRepositoryReturnsNoItems() {
        final Mountain mountain = Mountain.builder().build();
        when(mountainRouteRepository.findMountainRouteByMountain(Mountain.builder().build()))
                .thenReturn(Collections.emptyList());

        final List<MountainRoute> result = mountainRouteService.getAllByMountain(mountain);

        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetById() {
        final MountainRoute expectedResult = MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build();

        final Optional<MountainRoute> mountainRoute = Optional.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build());
        when(mountainRouteRepository.findById("id")).thenReturn(mountainRoute);

        final MountainRoute result = mountainRouteService.getById("id");

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetById_MountainRouteRepositoryReturnsAbsent() {
        when(mountainRouteRepository.findById("id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mountainRouteService.getById("id"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetAll() {
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build();
        when(mountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(routeRepository.findById("routeId")).thenReturn(Optional.of(Route.builder().build()));

        final Page<MountainRoute> mountainRoutes = new PageImpl<>(List.of(MountainRoute.builder()
                .mountain(Mountain.builder().build())
                .route(Route.builder().build())
                .build()));
        when(mountainRouteRepository.findAll(any(MountainRouteSpecification.class),
                any(Pageable.class))).thenReturn(mountainRoutes);

        final Page<MountainRoute> result = mountainRouteService.getAll("mountainId", "routeId",
                searchRequest);

    }

    @Test
    void testGetAll_MountainRepositoryReturnsAbsent() {
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build();
        when(mountainRepository.findById("mountainId")).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> mountainRouteService.getAll("mountainId", "routeId", searchRequest))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetAll_RouteRepositoryReturnsAbsent() {
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build();
        when(mountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(routeRepository.findById("routeId")).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> mountainRouteService.getAll("mountainId", "routeId", searchRequest))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testGetAll_MountainRouteRepositoryReturnsNoItems() {
        final SearchRequest searchRequest = SearchRequest.builder()
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build();
        when(mountainRepository.findById("mountainId")).thenReturn(Optional.of(Mountain.builder().build()));
        when(routeRepository.findById("routeId")).thenReturn(Optional.of(Route.builder().build()));
        when(mountainRouteRepository.findAll(any(MountainRouteSpecification.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        final Page<MountainRoute> result = mountainRouteService.getAll("mountainId", "routeId",
                searchRequest);
    }
}
