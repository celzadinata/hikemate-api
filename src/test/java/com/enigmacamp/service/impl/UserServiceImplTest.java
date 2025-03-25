package com.enigmacamp.service.impl;

import com.enigmacamp.constant.enums.UserRole;
import com.enigmacamp.model.entity.Role;
import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserServiceImpl userService;

    UserAccount userAccount;

    @BeforeEach
    void setUp(){
        userAccount = UserAccount.builder()
                .id("1")
                .email("mock@gmail.com")
                .password("mock123")
                .role(List.of(Role.builder()
                        .id("1")
                        .role(UserRole.HIKER)
                        .build()))
                .build();
    }

    @Test
    void createSuccess(){
        when(userAccountRepository.save(userAccount)).thenReturn(userAccount);
        UserAccount actualResponse = userService.create(userAccount);

        assertNotNull(actualResponse);
        assertEquals(userAccount, actualResponse);
    }

    @Test
    void loadUserById(){
        when(userAccountRepository.findById("1")).thenReturn(Optional.ofNullable(userAccount));
        UserAccount actualResponse = userService.loadUserById("1");

        assertNotNull(actualResponse);
        assertEquals(userAccount, actualResponse);
    }

    @Test
    void loadUserByUsername(){
        when(userAccountRepository.findByEmail("mock@gmail.com")).thenReturn(Optional.ofNullable(userAccount));
        UserAccount actualResponse = (UserAccount) userService.loadUserByUsername("mock@gmail.com");

        assertNotNull(actualResponse);
        assertEquals(userAccount, actualResponse);
    }
}
