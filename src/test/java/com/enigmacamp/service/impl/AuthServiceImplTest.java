package com.enigmacamp.service.impl;

import com.enigmacamp.constant.enums.UserRole;
import com.enigmacamp.model.dto.request.AuthRequest;
import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.request.NewUserRequest;
import com.enigmacamp.model.dto.request.RangerRequest;
import com.enigmacamp.model.dto.response.LoginResponse;
import com.enigmacamp.model.dto.response.RegisterResponse;
import com.enigmacamp.model.entity.Role;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.service.*;
import com.enigmacamp.utils.mapper.ImageMapper;
import com.enigmacamp.utils.validate_request.AuthValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RangerService rangerService;
    @Mock
    private HikerService hikerService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthValidation authValidation;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testRegisterHiker() {
        final NewUserRequest request = NewUserRequest.builder()
                .name("name")
                .phone("phone")
                .email("email@gmail.com")
                .password("password")
                .image(new MockMultipartFile("name", "content".getBytes()))
                .profilePicture(new MockMultipartFile("name", "content".getBytes()))
                .build();
        final RegisterResponse expectedResult = RegisterResponse.builder()
                .userId("id")
                .roles(List.of("value"))
                .build();
        when(passwordEncoder.encode("password")).thenReturn("password");
        when(roleService.getOrSaveRole(UserRole.HIKER)).thenReturn(Role.builder()
                .id("id")
                .role(UserRole.HIKER)
                .build());

        final UserAccount userAccount = UserAccount.builder()
                .id("id")
                .email("email@gmail.com")
                .password("password")
                .role(List.of(Role.builder()
                        .id("id")
                        .role(UserRole.HIKER)
                        .build()))
                .build();
        when(userService.create(UserAccount.builder()
                .id("id")
                .email("email@gmail.com")
                .password("password")
                .role(List.of(Role.builder()
                        .id("id")
                        .role(UserRole.HIKER)
                        .build()))
                .build())).thenReturn(userAccount);

        final RegisterResponse result = authService.registerHiker(request);

        assertThat(result).isEqualTo(expectedResult);
        verify(authValidation).validateCreateRequest(NewUserRequest.builder()
                .name("name")
                .phone("phone")
                .email("email@gmail.com")
                .password("password")
                .image(new MockMultipartFile("name", "content".getBytes()))
                .profilePicture(new MockMultipartFile("name", "content".getBytes()))
                .build());
        verify(hikerService).create(HikerRequest.builder()
                .name("name")
                .email("email@gmail.com")
                .phoneNumber("phone")
                .userAccount(UserAccount.builder()
                        .id("id")
                        .email("email@gmail.com")
                        .password("password")
                        .role(List.of(Role.builder()
                                .id("id")
                                .role(UserRole.HIKER)
                                .build()))
                        .build())
                .ktpImage(new MockMultipartFile("name", "content".getBytes()))
                .profilePicture(new MockMultipartFile("name", "content".getBytes()))
                .build());
    }

    @Test
    void testRegisterRanger() {
        final NewUserRequest request = NewUserRequest.builder()
                .name("name")
                .phone("phone")
                .email("email@gmail.com")
                .password("password")
                .image(new MockMultipartFile("name", "content".getBytes()))
                .profilePicture(new MockMultipartFile("name", "content".getBytes()))
                .build();
        final RegisterResponse expectedResult = RegisterResponse.builder()
                .userId("id")
                .roles(List.of("value"))
                .build();
        when(passwordEncoder.encode("password")).thenReturn("password");
        when(roleService.getOrSaveRole(UserRole.RANGER)).thenReturn(Role.builder()
                .id("id")
                .role(UserRole.HIKER)
                .build());

        final UserAccount userAccount = UserAccount.builder()
                .id("id")
                .email("email@gmail.com")
                .password("password")
                .role(List.of(Role.builder()
                        .id("id")
                        .role(UserRole.HIKER)
                        .build()))
                .build();
        when(userService.create(userAccount)).thenReturn(userAccount);

        final RegisterResponse result = authService.registerRanger(request);

        assertThat(result).isEqualTo(expectedResult);
        verify(authValidation).validateCreateRequest(NewUserRequest.builder()
                .name("name")
                .phone("phone")
                .email("email@gmail.com")
                .password("password")
                .image(new MockMultipartFile("name", "content".getBytes()))
                .profilePicture(new MockMultipartFile("name", "content".getBytes()))
                .build());
        verify(rangerService).create(RangerRequest.builder()
                .name("name")
                .phoneNumber("phone")
                .userAccount(UserAccount.builder()
                        .id("id")
                        .email("email@gmail.com")
                        .password("password")
                        .role(List.of(Role.builder()
                                .id("id")
                                .role(UserRole.HIKER)
                                .build()))
                        .build())
                .build());
    }

    @Test
    void testLogin() {
        final AuthRequest request = AuthRequest.builder()
                .email("email@gmail.com")
                .password("password")
                .build();
        final LoginResponse expectedResult = LoginResponse.builder()
                .token("token")
                .build();

        final Authentication authentication = new TestingAuthenticationToken("user", "pass", UserRole.HIKER.name());
        when(authenticationManager.authenticate(
                new TestingAuthenticationToken("user", "pass", UserRole.HIKER.name()))).thenReturn(authentication);

        when(jwtService.generateToken(UserAccount.builder()
                .id("id")
                .email("email@gmail.com")
                .password("password")
                .role(List.of(Role.builder()
                        .id("id")
                        .role(UserRole.HIKER)
                        .build()))
                .build())).thenReturn("token");

        final LoginResponse result = authService.login(request);

        assertThat(result).isEqualTo(expectedResult);
    }
}
