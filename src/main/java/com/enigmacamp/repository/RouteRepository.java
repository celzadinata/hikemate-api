package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.model.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
    @Override
    @Query("SELECT r FROM Route r WHERE r.id = ?1 AND r.deletedAt IS NULL")
    Optional<Route> findById(String id);

    @Override
    @Query("SELECT r FROM Route r WHERE r.deletedAt IS NULL")
    Page<Route> findAll(Pageable pageable);

    @Override
    @Query("SELECT r FROM Route r WHERE r.deletedAt IS NULL")
    List<Route> findAll();
}
