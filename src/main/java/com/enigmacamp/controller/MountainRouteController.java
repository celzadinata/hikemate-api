package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.request.MountainRouteRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.CommonResponse;
import com.enigmacamp.model.dto.response.MountainRouteResponse;
import com.enigmacamp.model.entity.MountainRoute;
import com.enigmacamp.service.MountainRouteService;
import com.enigmacamp.utils.mapper.MountainRouteMapper;
import com.enigmacamp.model.dto.response.PagingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.MOUNTAIN_ROUTE_API)
public class MountainRouteController {

    @Autowired
    private MountainRouteService mountainRouteService;

    @Autowired
    private MountainRouteMapper mountainRouteMapper;

    @PostMapping
    public ResponseEntity<CommonResponse<MountainRouteResponse>> createMountainRoute(@RequestBody MountainRouteRequest request) {
        MountainRoute mountainRoute = mountainRouteService.create(request);
        MountainRouteResponse response = mountainRouteMapper.entityToResponse(mountainRoute);

        CommonResponse<MountainRouteResponse> commonResponse = CommonResponse.<MountainRouteResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("New Mountain Route Added")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<CommonResponse<List<MountainRouteResponse>>> getAllMountainRoutes(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "id") String sortBy,
            @RequestParam(name = "mountainId", required = false) String mountainId,
            @RequestParam(name = "routeId", required = false) String routeId
    ) {

        SearchRequest searchRequest = SearchRequest.builder()
                .size(size)
                .page(Math.max(page - 1, 0))
                .direction(direction.toUpperCase())
                .sortBy(sortBy)
                .query(mountainId)
                .build();

        Page<MountainRouteResponse> mountainRouteResponses = mountainRouteService.getAll(mountainId, routeId, searchRequest).map(mountainRouteMapper::entityToResponse);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(mountainRouteResponses.getTotalPages())
                .totalElements(mountainRouteResponses.getTotalElements())
                .page(page)
                .size(size)
                .hasNext(mountainRouteResponses.hasNext())
                .hasPrevious(mountainRouteResponses.hasPrevious())
                .build();

        CommonResponse<List<MountainRouteResponse>> successResponse = CommonResponse
                .<List<MountainRouteResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Success fetching MountainRoute datas")
                .data(mountainRouteResponses.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content Type", "application/json")
                .body(successResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<MountainRouteResponse>> getMountainRouteById(@PathVariable String id) {
        MountainRoute mountainRoute = mountainRouteService.getById(id);
        MountainRouteResponse response = mountainRouteMapper.entityToResponse(mountainRoute);

        CommonResponse<MountainRouteResponse> commonResponse = CommonResponse.<MountainRouteResponse>builder()
                .status(HttpStatus.OK.value())
                .message("MountainRoute found")
                .data(response)
                .build();

        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteMountainRoute(@PathVariable String id) {
        MountainRoute mountainRoute = mountainRouteService.delete(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("MountainRoute successfully deleted")
                .data("MountainRoute with ID " + id + " successfully deleted.")
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
