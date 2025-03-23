package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.MountainRoute;
import com.enigmacamp.utils.specifications.MountainRouteSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MountainRouteRepository extends JpaRepository<MountainRoute, String>, JpaSpecificationExecutor<MountainRoute> {
    List<MountainRoute> findMountainRouteByMountain(Mountain mountain);
}
