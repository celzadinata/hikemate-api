package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RangerRepository extends JpaRepository<Ranger, String>, JpaSpecificationExecutor<Ranger> {
    @Override
    @Query("SELECT r FROM Ranger r WHERE r.id = :id AND r.deletedAt IS NULL")
    Optional<Ranger> findById(String id);

    @Override
    @Query("SELECT r FROM Ranger r WHERE r.deletedAt IS NULL")
    Page<Ranger> findAll(Pageable pageable);

    @Query("SELECT r FROM Ranger r WHERE r.userAccount = :userAccount AND r.deletedAt IS NULL")
    Optional<Ranger> findRangerByUserAccount(UserAccount userAccount);

    @Query("SELECT r FROM Ranger r WHERE r.mountain = :mountain AND r.deletedAt IS NULL")
    Optional<Ranger> findByMountain(Mountain mountain);
}
