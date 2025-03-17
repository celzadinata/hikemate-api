package com.enigmacamp.service.impl;

import com.enigmacamp.constant.enums.UserRole;
import com.enigmacamp.model.entity.Role;
import com.enigmacamp.repository.RoleRepository;
import com.enigmacamp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getOrSaveRole(UserRole role) {
        Optional<Role> optionalRole = roleRepository.findByRole(role);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }

        Role currentRole = Role.builder()
                .role(role)
                .build();
        return roleRepository.saveAndFlush(currentRole);
    }
}
