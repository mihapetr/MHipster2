package com.mihael.mhipster.repository;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Project entity.
 *
 * When extending this class, extend ProjectRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ProjectRepository extends ProjectRepositoryWithBagRelationships, JpaRepository<Project, Long> {
    @Query("select project from Project project where project.user.login = ?#{authentication.name}")
    List<Project> findByUserIsCurrentUser();

    /*default Optional<Project> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    @EntityGraph(attributePaths = "featureTsts")
    default List<Project> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }*/

    @EntityGraph(attributePaths = "featureTsts")
    @Query("select p from Project p where p.id = :id")
    Optional<Project> findOneWithEagerRelationships(Long id);

    @EntityGraph(attributePaths = "featureTsts")
    @Query("select p from Project p LEFT JOIN FETCH p.featureTsts")
    List<Project> findAllWithEagerRelationships();

    default Page<Project> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
