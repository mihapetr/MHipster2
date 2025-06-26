package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.FeatureTst;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface FeatureTstRepositoryWithBagRelationships {
    Optional<FeatureTst> fetchBagRelationships(Optional<FeatureTst> featureTst);

    List<FeatureTst> fetchBagRelationships(List<FeatureTst> featureTsts);

    Page<FeatureTst> fetchBagRelationships(Page<FeatureTst> featureTsts);
}
