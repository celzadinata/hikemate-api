package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Hiker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HikerRepository extends JpaRepository<Hiker, String>, JpaSpecificationExecutor<Hiker> {
}
