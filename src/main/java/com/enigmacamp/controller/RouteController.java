package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.request.RouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.CommonResponse;
import com.enigmacamp.model.dto.response.PagingResponse;
import com.enigmacamp.model.dto.response.RouteResponse;
import com.enigmacamp.service.RouteService;
import com.enigmacamp.utils.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.ROUTE_API)
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping
    public ResponseEntity<CommonResponse<RouteResponse>> addNewRoute(@RequestBody RouteRequest request) {
        RouteResponse routeResponse = routeService.create(request);
        CommonResponse<RouteResponse> response = CommonResponse.<RouteResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Route created successfully")
                .data(routeResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<RouteResponse>>> getAllRoutes(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sortBy", defaultValue = "route") String sortBy) {

        SearchRequest request = SearchRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .build();

        List<RouteResponse> routes = routeService.getAll(request);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Page<RouteResponse> pageResponse = new PageImpl<>(routes,pageable,routes.size());
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .page(page)
                .size(size)
                .hasNext(pageResponse.hasNext())
                .hasPrevious(pageResponse.hasPrevious())
                .build();

        CommonResponse<List<RouteResponse>> response = CommonResponse.<List<RouteResponse>>builder()
                .message("Routes fetched successfully")
                .status(HttpStatus.OK.value())
                .data(routes)
                .paging(pagingResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<RouteResponse>> getRouteById(@PathVariable String id) {
        RouteResponse route = routeService.getById(id);
        CommonResponse<RouteResponse> response = CommonResponse
                .<RouteResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Route found")
                .data(route)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<CommonResponse<RouteResponse>> updateRoute(@RequestBody RouteRequest request) {
        RouteResponse updatedRoute = routeService.update(request);
        CommonResponse<RouteResponse> response = CommonResponse
                .<RouteResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Route updated successfully")
                .data(updatedRoute)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
