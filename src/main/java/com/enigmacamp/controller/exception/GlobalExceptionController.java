package com.enigmacamp.controller.exception;

import com.enigmacamp.model.dto.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<CommonResponse<String>> handleResourceNotFoundException(RuntimeException exception){
        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<CommonResponse<String>> handleReponseException(ResponseStatusException exception){
        CommonResponse<String> response = CommonResponse.<String>builder()
                .status(exception.getStatusCode().value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(exception.getStatusCode()).body(response);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> constraintViolationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}
