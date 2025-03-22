package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.model.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HikerRepository extends JpaRepository<Hiker, String>, JpaSpecificationExecutor<Hiker> {
    Optional<Hiker> findHikerByUserAccount(UserAccount userAccount);
}
