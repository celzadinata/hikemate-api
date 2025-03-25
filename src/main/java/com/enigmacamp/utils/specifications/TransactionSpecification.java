package com.enigmacamp.utils.specifications;

import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.model.entity.Transaction;
import com.enigmacamp.service.HikerService;
import com.enigmacamp.service.MountainService;
import com.enigmacamp.service.RangerService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TransactionSpecification implements Specification<Transaction> {
    private final Boolean isUp;
    private final Boolean isDown;
    private final String status;
    private final Ranger ranger;
    private final Hiker hiker;
    private final Mountain mountain;
    private final String hikerName;

    @Autowired
    private RangerService rangerService;

    @Autowired
    private HikerService hikerService;

    @Autowired
    private MountainService mountainService;

    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (isUp != null) {
            predicates.add(criteriaBuilder.equal(root.get("isUp"), isUp));
        }
        if (isDown != null) {
            predicates.add(criteriaBuilder.equal(root.get("isDown"), isDown));
        }
        if (status != null && !status.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("status"), "%" + status.toUpperCase() + "%"));
        }
        if (ranger != null) {
            predicates.add(criteriaBuilder.equal(root.get("ranger"), ranger));
        }
        if (hiker != null) {
            predicates.add(criteriaBuilder.equal(root.get("hiker"), hiker));
        }
        if (mountain != null) {
            predicates.add(criteriaBuilder.equal(root.get("mountain"), mountain));
        }
        if (hikerName != null && !hikerName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.join("hiker").get("name"), "%" + hikerName + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
