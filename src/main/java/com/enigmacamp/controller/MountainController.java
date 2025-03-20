package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.request.MountainRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.CommonResponse;
import com.enigmacamp.model.dto.response.MountainResponse;
import com.enigmacamp.model.dto.response.PagingResponse;
import com.enigmacamp.service.MountainService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.MOUNTAIN_API)
public class MountainController {

    @Autowired
    private MountainService mountainService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addMountain(
            @RequestPart(name = "mountain") String request,
            @RequestPart(name = "image", required = false) MultipartFile image,
            @RequestPart(name = "base_camp_image", required = false) List<MultipartFile> baseCampImages
    ){
        try {
            MountainRequest mountainRequest = objectMapper.readValue(request, new TypeReference<>() {
            });
            mountainRequest.setImage(image);
            mountainRequest.setBaseCampImages(baseCampImages);
            MountainResponse mountainResponse = mountainService.create(mountainRequest);
            CommonResponse<MountainResponse> response = CommonResponse.<MountainResponse>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("New mountain added!")
                    .data(mountainResponse)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(response);
        } catch (Exception e) {
            System.out.println("error: {} " + e.getLocalizedMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body("Terjadi kesalahan pada PostMapping Mountain Controller: " +  e);
        }
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<CommonResponse<List<MountainResponse>>> getAllMountain(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "id") String sortBy,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "startPrice", required = false) String startPrice,
            @RequestParam(name = "endPrice", required = false) String endPrice,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "location", required = false) String location
    ) {

        SearchRequest searchRequest = SearchRequest.builder()
                .size(size)
                .page(Math.max(page - 1, 0))
                .direction(direction.toUpperCase())
                .sortBy(sortBy)
                .query(name)
                .build();

        Page<MountainResponse> mountainResponses = mountainService.getAll(name, startPrice, endPrice, status, location, searchRequest);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(mountainResponses.getTotalPages())
                .totalElements(mountainResponses.getTotalElements())
                .page(page)
                .size(size)
                .hasNext(mountainResponses.hasNext())
                .hasPrevious(mountainResponses.hasPrevious())
                .build();

        CommonResponse<List<MountainResponse>> response = CommonResponse.<List<MountainResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Success fetching mountain datas")
                .data(mountainResponses.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content Type", "application/json")
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<MountainResponse>> getMountainById(@PathVariable String id){
        MountainResponse mountainResponse = mountainService.getById(id);
        CommonResponse<MountainResponse> response = CommonResponse.<MountainResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Success fetching mountain with id: " + mountainResponse.getId())
                .data(mountainResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteMountainById(@PathVariable String id){
        mountainService.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Mountain deleted!")
                .data("Mountain with id: " + id + " deleted!")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(response);
    }
}
