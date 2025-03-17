package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.request.AuthRequest;
import com.enigmacamp.model.dto.request.NewUserRequest;
import com.enigmacamp.model.dto.response.CommonResponse;
import com.enigmacamp.model.dto.response.LoginResponse;
import com.enigmacamp.model.dto.response.RegisterResponse;
import com.enigmacamp.service.AuthService;
import com.enigmacamp.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = APIUrl.AUTH)
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerHiker(@RequestBody NewUserRequest request){
        RegisterResponse registerResponse = authService.registerHiker(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Register success!")
                .data(registerResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/ranger")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerRanger(@RequestBody NewUserRequest request){
        RegisterResponse registerResponse = authService.registerRanger(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Register success!")
                .data(registerResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody AuthRequest request){
        LoginResponse loginResponse = authService.login(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login success")
                .data(loginResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
