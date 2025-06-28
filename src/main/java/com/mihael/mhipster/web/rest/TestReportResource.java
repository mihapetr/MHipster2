package com.mihael.mhipster.web.rest;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.*;
import com.mihael.mhipster.repository.TestReportRepository;
import com.mihael.mhipster.repository.UserRepository;
import com.mihael.mhipster.security.SecurityUtils;
import com.mihael.mhipster.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mihael.mhipster.domain.TestReport}.
 */
@RestController
@RequestMapping("/api/test-reports")
@Transactional
public class TestReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(TestReportResource.class);

    private static final String ENTITY_NAME = "testReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestReportRepository testReportRepository;

    private final FeatureTstResource featureTstResource;
    private final ProjectResource projectResource;
    private final UserRepository userRepository;

    public TestReportResource(
        TestReportRepository testReportRepository,
        FeatureTstResource featureTstResource,
        ProjectResource projectResource,
        UserRepository userRepository
    ) {
        this.testReportRepository = testReportRepository;
        this.featureTstResource = featureTstResource;
        this.projectResource = projectResource;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /test-reports} : Create a new testReport.
     *
     * @param testReport the testReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testReport, or with status {@code 400 (Bad Request)} if the testReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestReport> createTestReport(@Valid @RequestBody TestReport testReport) throws URISyntaxException {
        LOG.debug("REST request to save TestReport : {}", testReport);
        if (testReport.getId() != null) {
            throw new BadRequestAlertException("A new testReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        testReport = testReportRepository.save(testReport);
        return ResponseEntity.created(new URI("/api/test-reports/" + testReport.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, testReport.getId().toString()))
            .body(testReport);
    }

    /**
     * Create a new FeatureTest. Attach it to the Project with given id. Check if user owns the Project.
     * Create a new TestReport and attach it to the new FeatureTest.
     *
     * @param id id of the Project the FeatureTest belongs to
     * @param testReport the report to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testReport, or with status {@code 400 (Bad Request)} if the testReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @MGenerated
    @PostMapping("/of-project/{id}")
    public ResponseEntity<TestReport> createTestReportOfProject(
        @PathVariable(value = "id", required = true) final Long id,
        @Valid @RequestBody TestReport testReport
    ) throws URISyntaxException {
        // get referenced project
        Project project = projectResource.getProject(id).getBody();

        if (!currentUserIsOwner(project.getUser().getId())) throw new BadRequestAlertException(
            "Unauthorized access.",
            ENTITY_NAME,
            "notowner"
        );

        // make empty CodeStats parent
        CodeStats codeStats = new CodeStats(); // cascade ensured

        // generate date
        // attach empty CodeStats : cascade ensured
        // link to project referenced in uri
        FeatureTst featureTst = new FeatureTst()
            .date(ZonedDateTime.now(ZoneId.systemDefault()).withNano(0))
            .parent(codeStats)
            .project(project);

        // link new report to new FeatureTst
        testReport.featureTst(featureTstResource.createFeatureTst(featureTst).getBody());

        return createTestReport(testReport);
    }

    boolean currentUserIsOwner(Long userId) {
        // get user
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow();
        User user = userRepository.findOneByLogin(login).orElseThrow();
        return user.getId().equals(userId);
    }

    /**
     * Create a new TestReport. Attach it to the feature test with given id.
     *
     * @param id id of the FeatureTest this TestReport belongs to.
     * @param testReport the report to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testReport, or with status {@code 400 (Bad Request)} if the testReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @MGenerated
    @PostMapping("/of-feature-test/{id}")
    public ResponseEntity<TestReport> createTestReportOfFeatureTest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestReport testReport
    ) throws URISyntaxException {
        return createTestReport(testReport);
    }

    /**
     * {@code PUT  /test-reports/:id} : Updates an existing testReport.
     *
     * @param id the id of the testReport to save.
     * @param testReport the testReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testReport,
     * or with status {@code 400 (Bad Request)} if the testReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestReport> updateTestReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestReport testReport
    ) throws URISyntaxException {
        LOG.debug("REST request to update TestReport : {}, {}", id, testReport);
        if (testReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        testReport = testReportRepository.save(testReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testReport.getId().toString()))
            .body(testReport);
    }

    /**
     * {@code PATCH  /test-reports/:id} : Partial updates given fields of an existing testReport, field will ignore if it is null
     *
     * @param id the id of the testReport to save.
     * @param testReport the testReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testReport,
     * or with status {@code 400 (Bad Request)} if the testReport is not valid,
     * or with status {@code 404 (Not Found)} if the testReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the testReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestReport> partialUpdateTestReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestReport testReport
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TestReport partially : {}, {}", id, testReport);
        if (testReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestReport> result = testReportRepository
            .findById(testReport.getId())
            .map(existingTestReport -> {
                if (testReport.getHtml() != null) {
                    existingTestReport.setHtml(testReport.getHtml());
                }
                if (testReport.getRuntimeRetention() != null) {
                    existingTestReport.setRuntimeRetention(testReport.getRuntimeRetention());
                }
                if (testReport.getMissedInstructions() != null) {
                    existingTestReport.setMissedInstructions(testReport.getMissedInstructions());
                }
                if (testReport.getInstructions() != null) {
                    existingTestReport.setInstructions(testReport.getInstructions());
                }
                if (testReport.getMissedBranches() != null) {
                    existingTestReport.setMissedBranches(testReport.getMissedBranches());
                }
                if (testReport.getBranches() != null) {
                    existingTestReport.setBranches(testReport.getBranches());
                }
                if (testReport.getMissedLines() != null) {
                    existingTestReport.setMissedLines(testReport.getMissedLines());
                }
                if (testReport.getLines() != null) {
                    existingTestReport.setLines(testReport.getLines());
                }
                if (testReport.getMissedMethods() != null) {
                    existingTestReport.setMissedMethods(testReport.getMissedMethods());
                }
                if (testReport.getMethods() != null) {
                    existingTestReport.setMethods(testReport.getMethods());
                }
                if (testReport.getMissedClasses() != null) {
                    existingTestReport.setMissedClasses(testReport.getMissedClasses());
                }
                if (testReport.getClasses() != null) {
                    existingTestReport.setClasses(testReport.getClasses());
                }

                return existingTestReport;
            })
            .map(testReportRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testReport.getId().toString())
        );
    }

    /**
     * {@code GET  /test-reports} : get all the testReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testReports in body.
     */
    @GetMapping("")
    public List<TestReport> getAllTestReports() {
        LOG.debug("REST request to get all TestReports");
        return testReportRepository.findAll();
    }

    /**
     * {@code GET  /test-reports/:id} : get the "id" testReport.
     *
     * @param id the id of the testReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestReport> getTestReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TestReport : {}", id);
        Optional<TestReport> testReport = testReportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(testReport);
    }

    /**
     * {@code DELETE  /test-reports/:id} : delete the "id" testReport.
     *
     * @param id the id of the testReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TestReport : {}", id);
        testReportRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
