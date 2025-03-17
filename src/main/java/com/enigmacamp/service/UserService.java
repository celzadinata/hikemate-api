package com.enigmacamp.service;

import com.enigmacamp.model.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount create(UserAccount userAccount);
    UserAccount loadUserById(String id);
}
