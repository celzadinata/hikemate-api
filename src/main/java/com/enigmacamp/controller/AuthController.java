package com.enigmacamp.controller;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.model.dto.request.AuthRequest;
import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.request.NewUserRequest;
import com.enigmacamp.model.dto.response.*;
import com.enigmacamp.service.AuthService;
import com.enigmacamp.service.JwtService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = APIUrl.AUTH)
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerHiker(
                @RequestPart(name = "hiker") String request,
                @RequestPart(name = "ktp", required = false)MultipartFile ktp,
                @RequestPart(name = "profile_picture", required = false)MultipartFile profilePicture
    ){
        try {
            NewUserRequest newUserRequest = objectMapper.readValue(request, new TypeReference<>() {
            });
            newUserRequest.setImage(ktp);
            newUserRequest.setProfilePicture(profilePicture);
            RegisterResponse registerResponse = authService.registerHiker(newUserRequest);
            CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Register success!")
                    .data(registerResponse)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(response);
        }catch (Exception e){
            System.out.println("error: {} " + e.getLocalizedMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body("Terjadi kesalahan pada PostMapping Auth Controller: " +  e);
        }
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
