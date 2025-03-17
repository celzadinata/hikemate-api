package com.enigmacamp.service;

import com.enigmacamp.model.dto.response.JwtClaims;
import com.enigmacamp.model.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);
    String refreshToken(String token);
    boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
