package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.FeatureTst;
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
public class FeatureTstRepositoryWithBagRelationshipsImpl implements FeatureTstRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String FEATURETSTS_PARAMETER = "featureTsts";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FeatureTst> fetchBagRelationships(Optional<FeatureTst> featureTst) {
        return featureTst.map(this::fetchFeatures);
    }

    @Override
    public Page<FeatureTst> fetchBagRelationships(Page<FeatureTst> featureTsts) {
        return new PageImpl<>(fetchBagRelationships(featureTsts.getContent()), featureTsts.getPageable(), featureTsts.getTotalElements());
    }

    @Override
    public List<FeatureTst> fetchBagRelationships(List<FeatureTst> featureTsts) {
        return Optional.of(featureTsts).map(this::fetchFeatures).orElse(Collections.emptyList());
    }

    FeatureTst fetchFeatures(FeatureTst result) {
        return entityManager
            .createQuery(
                "select featureTst from FeatureTst featureTst left join fetch featureTst.features where featureTst.id = :id",
                FeatureTst.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<FeatureTst> fetchFeatures(List<FeatureTst> featureTsts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, featureTsts.size()).forEach(index -> order.put(featureTsts.get(index).getId(), index));
        List<FeatureTst> result = entityManager
            .createQuery(
                "select featureTst from FeatureTst featureTst left join fetch featureTst.features where featureTst in :featureTsts",
                FeatureTst.class
            )
            .setParameter(FEATURETSTS_PARAMETER, featureTsts)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
