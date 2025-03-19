package com.enigmacamp.utils.specifications;

import com.enigmacamp.model.entity.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TransactionSpecification implements Specification<Transaction> {
    private final Boolean isUp;
    private final Boolean isDown;
    private final String status;

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

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
