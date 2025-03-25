package com.enigmacamp.utils.specifications;

import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.Ranger;
import com.enigmacamp.utils.UtilityTool;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MountainSpecification implements Specification<Mountain> {
    private final String name;
    private final String startPrice;
    private final String endPrice;
    private final String status;
    private final String location;

    @Override
    public Predicate toPredicate(Root<Mountain> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        if (status != null && !status.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("status"), "%" + status.toUpperCase() + "%"));
        }
        if (startPrice != null && endPrice != null) {
            predicates.add(criteriaBuilder.between(root.get("price"),startPrice, endPrice));
        }
        if (location != null && !location.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("location"), "%" + location + "%"));
        }

        predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
