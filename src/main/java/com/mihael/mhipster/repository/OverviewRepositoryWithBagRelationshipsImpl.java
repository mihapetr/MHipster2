package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.Overview;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class OverviewRepositoryWithBagRelationshipsImpl implements OverviewRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String OVERVIEWS_PARAMETER = "overviews";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Overview> fetchBagRelationships(Optional<Overview> overview) {
        return overview.map(this::fetchProjects);
    }

    @Override
    public Page<Overview> fetchBagRelationships(Page<Overview> overviews) {
        return new PageImpl<>(fetchBagRelationships(overviews.getContent()), overviews.getPageable(), overviews.getTotalElements());
    }

    @Override
    public List<Overview> fetchBagRelationships(List<Overview> overviews) {
        return Optional.of(overviews).map(this::fetchProjects).orElse(Collections.emptyList());
    }

    Overview fetchProjects(Overview result) {
        return entityManager
            .createQuery("select overview from Overview overview left join fetch overview.projects where overview.id = :id", Overview.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Overview> fetchProjects(List<Overview> overviews) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, overviews.size()).forEach(index -> order.put(overviews.get(index).getId(), index));
        List<Overview> result = entityManager
            .createQuery(
                "select overview from Overview overview left join fetch overview.projects where overview in :overviews",
                Overview.class
            )
            .setParameter(OVERVIEWS_PARAMETER, overviews)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
