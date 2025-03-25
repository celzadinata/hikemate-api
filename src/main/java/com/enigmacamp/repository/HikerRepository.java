package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.model.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HikerRepository extends JpaRepository<Hiker, String>, JpaSpecificationExecutor<Hiker> {
    @Override
    @Query("SELECT h FROM Hiker h WHERE h.id = ?1 AND h.deletedAt IS NULL")
    Optional<Hiker> findById(String id);

    @Override
    @Query("SELECT h FROM Hiker h WHERE h.deletedAt IS NULL")
    Page<Hiker> findAll(Pageable pageable);

    Optional<Hiker> findHikerByUserAccountAndDeletedAtIsNull(UserAccount userAccount);
}
