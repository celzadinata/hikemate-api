package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RangerRepository extends JpaRepository<Ranger, String>, JpaSpecificationExecutor<Ranger> {
    Optional<Ranger> findRangerByUserAccount(UserAccount userAccount);
    Optional<Ranger> findByMountain(Mountain mountain);
}
