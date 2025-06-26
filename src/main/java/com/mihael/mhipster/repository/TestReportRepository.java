package com.mihael.mhipster.repository;

import com.mihael.mhipster.domain.TestReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestReportRepository extends JpaRepository<TestReport, Long> {}
