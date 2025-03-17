package com.enigmacamp.service;

import com.enigmacamp.constant.enums.UserRole;
import com.enigmacamp.model.entity.Role;

public interface RoleService {
    Role getOrSaveRole(UserRole role);
}
