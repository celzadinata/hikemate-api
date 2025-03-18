package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Ranger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RangerRepository extends JpaRepository<Ranger, String>, JpaSpecificationExecutor<Ranger> {
}
