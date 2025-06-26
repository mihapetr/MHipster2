package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.Feature;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Feature entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    @Query("select feature from Feature feature where feature.user.login = ?#{authentication.name}")
    List<Feature> findByUserIsCurrentUser();
}
