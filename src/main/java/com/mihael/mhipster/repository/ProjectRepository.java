package com.mihael.mhipster.repository;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.CodeStats;
import com.mihael.mhipster.domain.Project;
import com.mihael.mhipster.service.dto.CodeStatsDBDTO;
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

    default Optional<Project> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Project> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    @MGenerated
    @Query(
        value = """
        WITH RankedFeatureTst AS (
          SELECT
        	ft.id AS feature_tst_id,
        	ft.project_id,
        	COUNT(rf.feature_id) AS feature_count,
        	ROW_NUMBER() OVER (PARTITION BY ft.project_id ORDER BY COUNT(rf.feature_id) DESC) AS rn
          FROM feature_tst ft
          LEFT JOIN rel_feature_tst__feature rf ON ft.id = rf.feature_tst_id
          GROUP BY ft.id, ft.project_id
        )
        SELECT
          AVG(cs.instructions) AS instructions,
          AVG(cs.branches) AS branches,
          AVG(cs.jhi_lines) AS 'lines',
          AVG(cs.methods) AS methods,
          AVG(cs.classes) AS classes,
          AVG(cs.dead_instructions) AS deadInstructions,
          AVG(cs.dead_branches) AS deadBranches,
          AVG(cs.dead_lines) AS deadLines,
          AVG(cs.dead_methods) AS deadMethods,
          AVG(cs.dead_classes) AS deadClasses
        FROM RankedFeatureTst rft
        JOIN feature_tst ft ON ft.id = rft.feature_tst_id
        JOIN code_stats cs ON cs.id = ft.parent_id
        WHERE rft.rn = 1;
           """,
        nativeQuery = true
    )
    CodeStatsDBDTO avgCodeStats();

    /*@EntityGraph(attributePaths = "featureTsts")
    @Query("select p from Project p where p.id = :id")
    Optional<Project> findOneWithEagerRelationships(Long id);

    @EntityGraph(attributePaths = "featureTsts")
    @Query("select p from Project p LEFT JOIN FETCH p.featureTsts")
    List<Project> findAllWithEagerRelationships();*/

    default Page<Project> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    Long id(Long id);
}
