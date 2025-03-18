package com.enigmacamp.service.impl;

import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.repository.UserAccountRepository;
import com.enigmacamp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserAccount create(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount loadUserById(String id) {
        return userAccountRepository.findById(id).orElseThrow(() -> new RuntimeException(("User Not Found")));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAccountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
    }
}
