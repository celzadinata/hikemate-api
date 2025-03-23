package com.enigmacamp.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigmacamp.model.dto.response.JwtClaims;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.service.HikerService;
import com.enigmacamp.service.JwtService;
import com.enigmacamp.service.RangerService;
import com.enigmacamp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${app.hikemate.jwt.app-name}")
    private String issuer;

    @Value("${app.hikemate.jwt.jwt-secret}")
    private String jwtSecret;

    @Value("${app.hikemate.jwt.expired}")
    private int jwtExpirationMs;

    @Autowired
    private UserService userService;

    @Autowired
    private HikerService hikerService;

    @Autowired
    private RangerService rangerService;


    @Override
    public String generateToken(UserAccount userAccount) {
        try {
            Result result = getResult(userAccount);
            return JWT.create()
                    .withIssuer(issuer)
                    .withClaim("userAccountId", userAccount.getId())
                    .withClaim("userLoggedInId", result.userLoggedInId())
                    .withClaim("name", result.userLoggedInName())
                    .withClaim("roles", userAccount.getRole().stream().map(role -> role.getRole().toString()).toList())
                    .withIssuedAt(new Date())
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpirationMs))
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch (JWTCreationException exception){
            throw new RuntimeException("You need to enable Algorithm.HMAC256");
        } catch (RuntimeException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }


    @Override
    public String refreshToken(String token) {
        token = "Bearer " + token;
        verifyJwtToken(token);
        JwtClaims jwtClaims = getClaimsByToken(token);
        UserAccount userAccount = userService.loadUserById(jwtClaims.getUserAccountId());

        return generateToken(userAccount);
    }

    @Override
    public boolean verifyJwtToken(String token) {
        String parsedJwt = parseJwt(token);
        JWTVerifier verifier = getVerifier(jwtSecret, issuer);
        try {
            verifier.verify(parsedJwt);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid or expired token!");
        }
        return true;
    }

    @Override
    public JwtClaims getClaimsByToken(String token) {
        String parseJwt = parseJwt(token);
        JWTVerifier verifier = getVerifier(jwtSecret, issuer);
        try {
            DecodedJWT decodedJWT = verifier.verify(parseJwt);
            String userAccountId = decodedJWT.getClaim("userAccountId").asString();
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            return JwtClaims.builder()
                    .userAccountId(userAccountId)
                    .roles(roles)
                    .build();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid or expired token!");
        }
    }

    private Result getResult(UserAccount userAccount) {
        String role = userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);
        String userLoggedInId = "";
        String userLoggedInName = "";

        if (Objects.equals(role, "RANGER")) {
            userLoggedInId =  rangerService.getByUserAccountEntity(userAccount).getId();
            userLoggedInName = rangerService.getByUserAccountEntity(userAccount).getName();
        } else if (Objects.equals(role, "HIKER")) {
            userLoggedInId = hikerService.getByUserAccountEntity(userAccount).getId();
            userLoggedInName = hikerService.getByUserAccountEntity(userAccount).getName();
        } else {
            userLoggedInId = userService.loadUserById(userAccount.getId()).getId();
            userLoggedInName = "super admin";
        }
        return new Result(userLoggedInId, userLoggedInName);
    }

    private record Result(String userLoggedInId, String userLoggedInName) {
    }

    public String parseJwt(String token){
        String[] tokenArr = token.split(" ");
        if (!tokenArr[0].equals("Bearer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Malformed token. Please include 'Bearer' prefix precisely in the token!");
        }
        int length = token.length();
        return token.substring(7, length);
    }

    public JWTVerifier getVerifier(String jwtSecret, String issuer){
        System.out.println(jwtSecret);
        try{
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();

        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid secret!");
        }
    }
}
