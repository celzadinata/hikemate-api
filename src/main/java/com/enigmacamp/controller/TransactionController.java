package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.request.TransactionRequest;
import com.enigmacamp.model.dto.response.CommonResponse;
import com.enigmacamp.model.dto.response.MountainResponse;
import com.enigmacamp.model.dto.response.TransactionResponse;
import com.enigmacamp.model.dto.response.PagingResponse;
import com.enigmacamp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.TRANSACTION_API)
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> addTransaction(@RequestBody TransactionRequest request){
        TransactionResponse transactionResponse = transactionService.create(request);
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("New Transaction added!")
                .data(transactionResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Content-Type", "application/json")
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "id") String sortBy,
            @RequestParam(name = "isUp", required = false) Boolean isUp,
            @RequestParam(name = "isDown", required = false) Boolean isDown,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "ranger_id", required = false) String rangerId,
            @RequestParam(name = "hiker_id", required = false) String hikerId,
            @RequestParam(name = "mountain_id", required = false) String mountainId,
            @RequestParam(name = "hiker_name", required = false) String hikerName
    ){
        SearchRequest searchRequest = SearchRequest.builder()
                .size(size)
                .page(Math.max(page - 1, 0))
                .direction(direction.toUpperCase())
                .sortBy(sortBy)
                .query(status)
                .build();

        Page<TransactionResponse> transactionResponses = transactionService.getAll(isUp, isDown, status, rangerId, hikerId, mountainId, hikerName, searchRequest);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(transactionResponses.getTotalPages())
                .totalElements(transactionResponses.getTotalElements())
                .page(page)
                .size(size)
                .hasNext(transactionResponses.hasNext())
                .hasPrevious(transactionResponses.hasPrevious())
                .build();

        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Success fetching transaction datas")
                .data(transactionResponses.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content Type", "application/json")
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TransactionResponse>> getTransactionById(@PathVariable String id){
        TransactionResponse transactionResponse = transactionService.getById(id);
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Success fetching transaction with id: " + transactionResponse.getTransactionId())
                .data(transactionResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(response);
    }

    @GetMapping("/statistic/{month}/{year}/{mountainId}")
    public ResponseEntity<?> getTransactionStatistic(
            @PathVariable Integer month,
            @PathVariable Integer year,
            @PathVariable String mountainId,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ){
        SearchRequest searchRequest = SearchRequest.builder()
                .size(size)
                .page(Math.max(page - 1, 0))
                .direction("asc")
                .sortBy("id")
                .build();

        Page<TransactionResponse> transactionResponses = transactionService.getTransactionByMonthAndYear(month, year, mountainId, searchRequest);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(transactionResponses.getTotalPages())
                .totalElements(transactionResponses.getTotalElements())
                .page(page)
                .size(size)
                .hasNext(transactionResponses.hasNext())
                .hasPrevious(transactionResponses.hasPrevious())
                .build();

        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Success fetching transaction statistic")
                .data(transactionResponses.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content Type", "application/json")
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse<TransactionResponse>> updateHikerStatus(@PathVariable String id){
        TransactionResponse transactionResponse = transactionService.updateHikerStatus(id);
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Success updating hiker status with transaction id: " + transactionResponse.getTransactionId() + "! isUp: " + transactionResponse.getIsUp() + ", isDown: " + transactionResponse.getIsDown())
                .data(transactionResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(response);
    }
}
