package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.RouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.RouteResponse;
import com.enigmacamp.model.entity.Route;
import com.enigmacamp.repository.RouteRepository;
import com.enigmacamp.utils.mapper.RouteMapper;
import com.enigmacamp.utils.validate_request.RouteValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    private RouteRepository routeRepository;
    @Mock
    private RouteMapper routeMapper;
    @Mock
    private RouteValidation routeValidation;

    @InjectMocks
    private RouteServiceImpl routeService;

    @Test
    void testCreate() {
        RouteRequest request = RouteRequest.builder()
                .id("id")
                .routeName("route")
                .build();
        RouteResponse expectedResult = RouteResponse.builder().build();
        when(routeRepository.save(Route.builder()
                .route("route")
                .build())).thenReturn(Route.builder()
                .route("route")
                .build());
        when(routeMapper.entityToResponse(Route.builder()
                .route("route")
                .build())).thenReturn(RouteResponse.builder().build());

        RouteResponse result = routeService.create(request);

        assertThat(result).isEqualTo(expectedResult);
        verify(routeValidation).validateCreateRequest(RouteRequest.builder()
                .id("id")
                .routeName("route")
                .build());
    }

    @Test
    void testGetAll() {
        SearchRequest searchRequest = SearchRequest.builder()
                .page(1)
                .size(10)
                .direction("direction")
                .sortBy("sortBy")
                .build();
        List<RouteResponse> expectedResult = List.of(RouteResponse.builder().build());

        List<Route> routeList = List.of(Route.builder()
                .route("route")
                .build());
        when(routeRepository.findAll()).thenReturn(routeList);

        when(routeMapper.entityToResponse(Route.builder()
                .route("route")
                .build())).thenReturn(RouteResponse.builder().build());

        List<RouteResponse> result = routeService.getAll(searchRequest);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetById() {
        RouteResponse expectedResult = RouteResponse.builder().build();
        Optional<Route> route = Optional.of(Route.builder()
                .route("route")
                .build());
        when(routeRepository.findById("id")).thenReturn(route);

        when(routeMapper.entityToResponse(Route.builder()
                .route("route")
                .build())).thenReturn(RouteResponse.builder().build());

        RouteResponse result = routeService.getById("id");

        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    void testUpdate() {
        RouteRequest request = RouteRequest.builder()
                .id("id")
                .routeName("route")
                .build();
        RouteResponse expectedResult = RouteResponse.builder().build();

        Optional<Route> route = Optional.of(Route.builder()
                .route("route")
                .build());
        when(routeRepository.findById("id")).thenReturn(route);

        when(routeRepository.save(Route.builder()
                .route("route")
                .build())).thenReturn(Route.builder()
                .route("route")
                .build());
        when(routeMapper.entityToResponse(Route.builder()
                .route("route")
                .build())).thenReturn(RouteResponse.builder().build());

        RouteResponse result = routeService.update(request);

        assertThat(result).isEqualTo(expectedResult);
        verify(routeValidation).validateUpdateRequest(RouteRequest.builder()
                .id("id")
                .routeName("route")
                .build());
    }

    @Test
    void testDelete() {
        RouteResponse expectedResult = RouteResponse.builder().build();

        Optional<Route> route = Optional.of(Route.builder()
                .route("route")
                .build());
        when(routeRepository.findById("id")).thenReturn(route);

        when(routeMapper.entityToResponse(Route.builder()
                .route("route")
                .build())).thenReturn(RouteResponse.builder().build());

        RouteResponse result = routeService.delete("id");

        assertThat(result).isEqualTo(expectedResult);
        verify(routeRepository).saveAndFlush(Route.builder()
                .route("route")
                .build());
    }


    @Test
    void testGetByIdEntity() {
        Route expectedResult = Route.builder()
                .route("route")
                .build();

        Optional<Route> route = Optional.of(Route.builder()
                .route("route")
                .build());
        when(routeRepository.findById("id")).thenReturn(route);
        Route result = routeService.getByIdEntity("id");
        assertThat(result).isEqualTo(expectedResult);
    }
}
