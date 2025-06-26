package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.FeatureTst;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeatureTst entity.
 *
 * When extending this class, extend FeatureTstRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface FeatureTstRepository extends FeatureTstRepositoryWithBagRelationships, JpaRepository<FeatureTst, Long> {
    default Optional<FeatureTst> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<FeatureTst> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<FeatureTst> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
