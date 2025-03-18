package com.enigmacamp.service;

import com.enigmacamp.model.dto.request.AuthRequest;
import com.enigmacamp.model.dto.request.NewUserRequest;
import com.enigmacamp.model.dto.response.LoginResponse;
import com.enigmacamp.model.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerHiker(NewUserRequest request);
    RegisterResponse registerRanger(NewUserRequest request);
    LoginResponse login(AuthRequest request);
}
