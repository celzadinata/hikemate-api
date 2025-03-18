package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, String>, JpaSpecificationExecutor<Mountain> {

}
