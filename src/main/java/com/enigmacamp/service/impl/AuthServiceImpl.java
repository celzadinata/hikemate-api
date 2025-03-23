package com.enigmacamp.service.impl;

import com.enigmacamp.constant.Tables;
import com.enigmacamp.constant.enums.UserRole;
import com.enigmacamp.model.dto.request.AuthRequest;
import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.request.NewUserRequest;
import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.response.HikerResponse;
import com.enigmacamp.model.dto.response.LoginResponse;
import com.enigmacamp.model.dto.response.RegisterResponse;
import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.model.entity.Image;
import com.enigmacamp.model.entity.Role;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.service.*;
import com.enigmacamp.utils.mapper.HikerMapper;
import com.enigmacamp.utils.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RangerService rangerService;

    @Autowired
    private HikerService hikerService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageMapper imageMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerHiker(NewUserRequest request) {
        String password = passwordEncoder.encode(request.getPassword());
        Role hikerRole = roleService.getOrSaveRole(UserRole.HIKER);

        UserAccount account = createUserAccount(request, password, hikerRole);
        account = userService.create(account);

        HikerRequest hikerRequest = createHikerRequest(request, account);
        hikerService.create(hikerRequest);

        return RegisterResponse.builder()
                .userId(account.getId())
                .roles(List.of(hikerRole.getRole().toString()))
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerRanger(NewUserRequest request) {
        String password = passwordEncoder.encode(request.getPassword());
        Role hikerRole = roleService.getOrSaveRole(UserRole.RANGER);

        UserAccount account = createUserAccount(request, password, hikerRole);
        account = userService.create(account);

        RangerRequest rangerRequest = createRangerRequest(request, account);
        rangerService.create(rangerRequest);

        return RegisterResponse.builder()
                .userId(account.getId())
                .roles(List.of(hikerRole.getRole().toString()))
                .build();
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();

        String token = jwtService.generateToken(userAccount);
        return LoginResponse.builder()
                .token(token)
                .build();
    }

    private UserAccount createUserAccount(NewUserRequest request, String password, Role role) {
        return UserAccount.builder()
                .email(request.getEmail())
                .password(password)
                .role(List.of(role))
                .build();
    }

    private HikerRequest createHikerRequest(NewUserRequest request, UserAccount account) {
        return HikerRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhone())
                .ktpImage(request.getImage())
                .profilePicture(request.getProfilePicture())
                .userAccount(account)
                .build();
    }

    private RangerRequest createRangerRequest(NewUserRequest request, UserAccount account) {
        return RangerRequest.builder()
                .name(request.getName())
                .phoneNumber(request.getPhone())
                .userAccount(account)
                .build();
    }
}
