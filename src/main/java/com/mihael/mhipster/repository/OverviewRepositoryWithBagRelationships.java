package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.Overview;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface OverviewRepositoryWithBagRelationships {
    Optional<Overview> fetchBagRelationships(Optional<Overview> overview);

    List<Overview> fetchBagRelationships(List<Overview> overviews);

    Page<Overview> fetchBagRelationships(Page<Overview> overviews);
}
