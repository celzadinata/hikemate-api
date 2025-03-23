package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.request.AssignHikerRequest;
import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.CommonResponse;
import com.enigmacamp.model.dto.response.HikerResponse;
import com.enigmacamp.model.dto.response.PagingResponse;
import com.enigmacamp.service.HikerService;
import com.enigmacamp.utils.PagingUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.HIKER_API)
public class HikerController {
    @Autowired
    private HikerService hikerService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<CommonResponse<HikerResponse>> addHiker(
            @RequestBody AssignHikerRequest request
    ){
        HikerRequest hikerRequest = HikerRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .build();
        HikerResponse hikerResponse = hikerService.create(hikerRequest);
        CommonResponse<HikerResponse> response = CommonResponse.<HikerResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Hiker added successfully")
                .data(hikerResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<HikerResponse>>> getAllHikers(
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

        Page<HikerResponse> hikers = hikerService.getAllHikers(request);
        if (hikers.isEmpty()){
            CommonResponse<List<HikerResponse>> errorResponse = CommonResponse
                    .<List<HikerResponse>>builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Data Tidak Ketemu")
                    .data(null)
                    .build();
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        PagingResponse paging = PagingResponse.builder()
                .totalPages(hikers.getTotalPages())
                .totalElements(hikers.getTotalElements())
                .page(page)
                .size(size)
                .hasNext(hikers.hasNext())
                .hasPrevious(hikers.hasPrevious())
                .build();

        CommonResponse<List<HikerResponse>> response = CommonResponse
                .<List<HikerResponse>>builder()
                .message("tampil data")
                .status(HttpStatus.OK.value())
                .data(hikers.getContent())
                .paging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<HikerResponse>> getHikerById(@PathVariable String id) {
        HikerResponse hiker = hikerService.getById(id);
        CommonResponse<HikerResponse> response = CommonResponse.
                <HikerResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Data ID ditemukan")
                .data(hiker)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<CommonResponse<HikerResponse>> updateHiker(
            @RequestPart(name = "hiker") String request,
            @RequestPart(name = "ktp", required = false) MultipartFile ktp,
            @RequestPart(name = "profile_picture", required = false) MultipartFile profilePicture
    ) {
        try {
            HikerRequest hikerRequest = objectMapper.readValue(request, new TypeReference<>() {});
            hikerRequest.setKtpImage(ktp);
            hikerRequest.setProfilePicture(profilePicture);
            HikerResponse updatedHiker = hikerService.updateHiker(hikerRequest);
            CommonResponse<HikerResponse> response = CommonResponse
                    .<HikerResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Hiker updated successfully")
                    .data(updatedHiker)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHiker(@PathVariable String id){
        hikerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
