package com.enigmacamp.service.impl;

import com.enigmacamp.constant.enums.UserRole;
import com.enigmacamp.model.entity.Role;
import com.enigmacamp.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void testGetOrSaveRole() {
        Role expectedResult = Role.builder()
                .role(UserRole.HIKER)
                .build();

        Optional<Role> role = Optional.of(Role.builder()
                .role(UserRole.HIKER)
                .build());
        when(roleRepository.findByRole(UserRole.HIKER)).thenReturn(role);

        Role result = roleService.getOrSaveRole(UserRole.HIKER);

        assertThat(result).isEqualTo(expectedResult);
    }
}
