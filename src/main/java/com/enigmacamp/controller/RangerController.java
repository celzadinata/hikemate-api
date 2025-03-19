package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.request.AssignRangerRequest;
import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.CommonResponse;
import com.enigmacamp.model.dto.response.PagingResponse;
import com.enigmacamp.model.dto.response.RangerResponse;
import com.enigmacamp.model.dto.response.RegisterResponse;
import com.enigmacamp.service.RangerService;
import com.enigmacamp.utils.mapper.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.RANGER_API)
public class RangerController {
    @Autowired
    private RangerService rangerService;

    @PostMapping
    public ResponseEntity<CommonResponse<RangerResponse>> addNewRanger(@RequestBody AssignRangerRequest req) {
        RangerRequest request = RangerRequest.builder()
                .name(req.getName())
                .phoneNumber(req.getPhoneNumber())
                .build();
        RangerResponse registerResponse = rangerService.create(request);
        CommonResponse<RangerResponse> response = CommonResponse.<RangerResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Register success!")
                .data(registerResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<RangerResponse>>> getAllRangers(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy) {

        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchRequest request = SearchRequest.builder()
                .query(search)
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .build();

        Page<RangerResponse> rangers = rangerService.getAllRangers(request);
        if (rangers.isEmpty()) {
            CommonResponse<List<RangerResponse>> errorResponse = CommonResponse
                    .<List<RangerResponse>>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Data tidak ketemu")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        PagingResponse paging = PagingResponse.builder()
                .totalPages(rangers.getTotalPages())
                .totalElements(rangers.getTotalElements())
                .page(page)
                .size(size)
                .hasNext(rangers.hasNext())
                .hasPrevious(rangers.hasPrevious())
                .build();

        CommonResponse<List<RangerResponse>> response = CommonResponse
                .<List<RangerResponse>>builder()
                .message("Tampil data")
                .status(HttpStatus.OK.value())
                .data(rangers.getContent())
                .paging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<RangerResponse>> getRangerById(@PathVariable String id) {
        RangerResponse ranger = rangerService.getById(id);
        CommonResponse<RangerResponse> response = CommonResponse.
                <RangerResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Data ID ditemukan")
                .data(ranger)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<RangerResponse>> updateRanger(
            @PathVariable String id,
            @RequestBody RangerRequest request) {
        request.setId(id);
        RangerResponse updatedRanger = rangerService.updateRanger(request);
        CommonResponse<RangerResponse> response = CommonResponse
                .<RangerResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Ranger updated successfully")
                .data(updatedRanger)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRanger(@PathVariable String id) {
        rangerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

