package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.CodeStats;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CodeStats entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodeStatsRepository extends JpaRepository<CodeStats, Long> {}
