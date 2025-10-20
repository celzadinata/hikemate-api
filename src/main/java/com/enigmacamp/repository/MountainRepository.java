package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MountainRepository extends JpaRepository<Mountain, String>, JpaSpecificationExecutor<Mountain> {

    @Override
    @Query("SELECT m FROM Mountain m WHERE m.id = :id AND m.deletedAt IS NULL")
    Optional<Mountain> findById(String id);

}
