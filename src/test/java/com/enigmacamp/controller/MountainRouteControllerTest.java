package com.enigmacamp.controller;

import com.enigmacamp.model.dto.request.MountainRouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.MountainRouteResponse;
import com.enigmacamp.model.entity.MountainRoute;
import com.enigmacamp.service.MountainRouteService;
import com.enigmacamp.utils.mapper.MountainRouteMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MountainRouteController.class)
class MountainRouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MountainRouteService mockMountainRouteService;
    @Mock
    private MountainRouteMapper mockMountainRouteMapper;

    @Test
    void testCreateMountainRoute() throws Exception {
        // Setup
        when(mockMountainRouteService.create(MountainRouteRequest.builder().build()))
                .thenReturn(MountainRoute.builder().build());
        when(mockMountainRouteMapper.entityToResponse(MountainRoute.builder().build()))
                .thenReturn(MountainRouteResponse.builder().build());

        // Run the test and verify the results
        mockMvc.perform(post("/api/v1/mountain-routes")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetAllMountainRoutes() throws Exception {
        // Setup
        // Configure MountainRouteService.getAll(...).
        final Page<MountainRoute> mountainRoutes = new PageImpl<>(List.of(MountainRoute.builder().build()));
        when(mockMountainRouteService.getAll("mountainId", "routeId", SearchRequest.builder()
                .query("mountainId")
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build())).thenReturn(mountainRoutes);

        when(mockMountainRouteMapper.entityToResponse(MountainRoute.builder().build()))
                .thenReturn(MountainRouteResponse.builder().build());

        // Run the test and verify the results
        mockMvc.perform(get("/api/v1/mountain-routes")
                        .param("page", "0")
                        .param("size", "0")
                        .param("direction", "direction")
                        .param("sort", "sortBy")
                        .param("mountainId", "mountainId")
                        .param("routeId", "routeId")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetAllMountainRoutes_MountainRouteServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockMountainRouteService.getAll("mountainId", "routeId", SearchRequest.builder()
                .query("mountainId")
                .page(0)
                .size(0)
                .direction("direction")
                .sortBy("sortBy")
                .build())).thenReturn(new PageImpl<>(Collections.emptyList()));

        // Run the test and verify the results
        mockMvc.perform(get("/api/v1/mountain-routes")
                        .param("page", "0")
                        .param("size", "0")
                        .param("direction", "direction")
                        .param("sort", "sortBy")
                        .param("mountainId", "mountainId")
                        .param("routeId", "routeId")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testGetMountainRouteById() throws Exception {
        // Setup
        when(mockMountainRouteService.getById("id")).thenReturn(MountainRoute.builder().build());
        when(mockMountainRouteMapper.entityToResponse(MountainRoute.builder().build()))
                .thenReturn(MountainRouteResponse.builder().build());

        // Run the test and verify the results
        mockMvc.perform(get("/api/v1/mountain-routes/{id}", "id")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testDeleteMountainRoute() throws Exception {
        // Setup
        when(mockMountainRouteService.delete("id")).thenReturn(MountainRoute.builder().build());

        // Run the test and verify the results
        mockMvc.perform(delete("/api/v1/mountain-routes/{id}", "id")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }
}
