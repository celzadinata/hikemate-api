package com.enigmacamp.utils.specifications;

import com.enigmacamp.model.entity.Mountain;
import com.enigmacamp.model.entity.MountainRoute;
import com.enigmacamp.model.entity.Route;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MountainRouteSpecification implements Specification<MountainRoute> {
    private final Mountain mountain;
    private final Route route;

    @Override
    public Predicate toPredicate(Root<MountainRoute> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (route != null) {
            predicates.add(criteriaBuilder.equal(root.get("route"), route));
        }
        if (mountain != null) {
            predicates.add(criteriaBuilder.equal(root.get("mountain"), mountain));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
