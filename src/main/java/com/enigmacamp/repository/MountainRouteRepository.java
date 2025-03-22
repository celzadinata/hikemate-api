package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.MountainRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MountainRouteRepository extends JpaRepository<MountainRoute, String> {
    List<MountainRoute> findMountainRouteByMountain(Mountain mountain);
}
