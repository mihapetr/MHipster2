package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.Overview;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Overview entity.
 *
 * When extending this class, extend OverviewRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface OverviewRepository extends OverviewRepositoryWithBagRelationships, JpaRepository<Overview, Long> {
    @Query("select overview from Overview overview where overview.user.login = ?#{authentication.name}")
    List<Overview> findByUserIsCurrentUser();

    default Optional<Overview> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Overview> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Overview> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
