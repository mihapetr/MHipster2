package com.mihael.mhipster.web.rest;

import static com.mihael.mhipster.domain.TestReportAsserts.*;
import static com.mihael.mhipster.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mihael.mhipster.IntegrationTest;
import com.mihael.mhipster.domain.FeatureTst;
import com.mihael.mhipster.domain.TestReport;
import com.mihael.mhipster.repository.TestReportRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TestReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestReportResourceIT {

    private static final String DEFAULT_HTML = "AAAAAAAAAA";
    private static final String UPDATED_HTML = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RUNTIME_RETENTION = false;
    private static final Boolean UPDATED_RUNTIME_RETENTION = true;

    private static final Integer DEFAULT_MISSED_INSTRUCTIONS = 1;
    private static final Integer UPDATED_MISSED_INSTRUCTIONS = 2;

    private static final Integer DEFAULT_INSTRUCTIONS = 1;
    private static final Integer UPDATED_INSTRUCTIONS = 2;

    private static final Integer DEFAULT_MISSED_BRANCHES = 1;
    private static final Integer UPDATED_MISSED_BRANCHES = 2;

    private static final Integer DEFAULT_BRANCHES = 1;
    private static final Integer UPDATED_BRANCHES = 2;

    private static final Integer DEFAULT_MISSED_LINES = 1;
    private static final Integer UPDATED_MISSED_LINES = 2;

    private static final Integer DEFAULT_LINES = 1;
    private static final Integer UPDATED_LINES = 2;

    private static final Integer DEFAULT_MISSED_METHODS = 1;
    private static final Integer UPDATED_MISSED_METHODS = 2;

    private static final Integer DEFAULT_METHODS = 1;
    private static final Integer UPDATED_METHODS = 2;

    private static final Integer DEFAULT_MISSED_CLASSES = 1;
    private static final Integer UPDATED_MISSED_CLASSES = 2;

    private static final Integer DEFAULT_CLASSES = 1;
    private static final Integer UPDATED_CLASSES = 2;

    private static final String ENTITY_API_URL = "/api/test-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TestReportRepository testReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestReportMockMvc;

    private TestReport testReport;

    private TestReport insertedTestReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestReport createEntity(EntityManager em) {
        TestReport testReport = new TestReport()
            .html(DEFAULT_HTML)
            .runtimeRetention(DEFAULT_RUNTIME_RETENTION)
            .missedInstructions(DEFAULT_MISSED_INSTRUCTIONS)
            .instructions(DEFAULT_INSTRUCTIONS)
            .missedBranches(DEFAULT_MISSED_BRANCHES)
            .branches(DEFAULT_BRANCHES)
            .missedLines(DEFAULT_MISSED_LINES)
            .lines(DEFAULT_LINES)
            .missedMethods(DEFAULT_MISSED_METHODS)
            .methods(DEFAULT_METHODS)
            .missedClasses(DEFAULT_MISSED_CLASSES)
            .classes(DEFAULT_CLASSES);
        // Add required entity
        FeatureTst featureTst;
        if (TestUtil.findAll(em, FeatureTst.class).isEmpty()) {
            featureTst = FeatureTstResourceIT.createEntity(em);
            em.persist(featureTst);
            em.flush();
        } else {
            featureTst = TestUtil.findAll(em, FeatureTst.class).get(0);
        }
        testReport.setFeatureTst(featureTst);
        return testReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestReport createUpdatedEntity(EntityManager em) {
        TestReport updatedTestReport = new TestReport()
            .html(UPDATED_HTML)
            .runtimeRetention(UPDATED_RUNTIME_RETENTION)
            .missedInstructions(UPDATED_MISSED_INSTRUCTIONS)
            .instructions(UPDATED_INSTRUCTIONS)
            .missedBranches(UPDATED_MISSED_BRANCHES)
            .branches(UPDATED_BRANCHES)
            .missedLines(UPDATED_MISSED_LINES)
            .lines(UPDATED_LINES)
            .missedMethods(UPDATED_MISSED_METHODS)
            .methods(UPDATED_METHODS)
            .missedClasses(UPDATED_MISSED_CLASSES)
            .classes(UPDATED_CLASSES);
        // Add required entity
        FeatureTst featureTst;
        if (TestUtil.findAll(em, FeatureTst.class).isEmpty()) {
            featureTst = FeatureTstResourceIT.createUpdatedEntity(em);
            em.persist(featureTst);
            em.flush();
        } else {
            featureTst = TestUtil.findAll(em, FeatureTst.class).get(0);
        }
        updatedTestReport.setFeatureTst(featureTst);
        return updatedTestReport;
    }

    @BeforeEach
    public void initTest() {
        testReport = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTestReport != null) {
            testReportRepository.delete(insertedTestReport);
            insertedTestReport = null;
        }
    }

    @Test
    @Transactional
    void createTestReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TestReport
        var returnedTestReport = om.readValue(
            restTestReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testReport)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TestReport.class
        );

        // Validate the TestReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTestReportUpdatableFieldsEquals(returnedTestReport, getPersistedTestReport(returnedTestReport));

        insertedTestReport = returnedTestReport;
    }

    @Test
    @Transactional
    void createTestReportWithExistingId() throws Exception {
        // Create the TestReport with an existing ID
        testReport.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testReport)))
            .andExpect(status().isBadRequest());

        // Validate the TestReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestReports() throws Exception {
        // Initialize the database
        insertedTestReport = testReportRepository.saveAndFlush(testReport);

        // Get all the testReportList
        restTestReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].html").value(hasItem(DEFAULT_HTML)))
            .andExpect(jsonPath("$.[*].runtimeRetention").value(hasItem(DEFAULT_RUNTIME_RETENTION)))
            .andExpect(jsonPath("$.[*].missedInstructions").value(hasItem(DEFAULT_MISSED_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].missedBranches").value(hasItem(DEFAULT_MISSED_BRANCHES)))
            .andExpect(jsonPath("$.[*].branches").value(hasItem(DEFAULT_BRANCHES)))
            .andExpect(jsonPath("$.[*].missedLines").value(hasItem(DEFAULT_MISSED_LINES)))
            .andExpect(jsonPath("$.[*].lines").value(hasItem(DEFAULT_LINES)))
            .andExpect(jsonPath("$.[*].missedMethods").value(hasItem(DEFAULT_MISSED_METHODS)))
            .andExpect(jsonPath("$.[*].methods").value(hasItem(DEFAULT_METHODS)))
            .andExpect(jsonPath("$.[*].missedClasses").value(hasItem(DEFAULT_MISSED_CLASSES)))
            .andExpect(jsonPath("$.[*].classes").value(hasItem(DEFAULT_CLASSES)));
    }

    @Test
    @Transactional
    void getTestReport() throws Exception {
        // Initialize the database
        insertedTestReport = testReportRepository.saveAndFlush(testReport);

        // Get the testReport
        restTestReportMockMvc
            .perform(get(ENTITY_API_URL_ID, testReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testReport.getId().intValue()))
            .andExpect(jsonPath("$.html").value(DEFAULT_HTML))
            .andExpect(jsonPath("$.runtimeRetention").value(DEFAULT_RUNTIME_RETENTION))
            .andExpect(jsonPath("$.missedInstructions").value(DEFAULT_MISSED_INSTRUCTIONS))
            .andExpect(jsonPath("$.instructions").value(DEFAULT_INSTRUCTIONS))
            .andExpect(jsonPath("$.missedBranches").value(DEFAULT_MISSED_BRANCHES))
            .andExpect(jsonPath("$.branches").value(DEFAULT_BRANCHES))
            .andExpect(jsonPath("$.missedLines").value(DEFAULT_MISSED_LINES))
            .andExpect(jsonPath("$.lines").value(DEFAULT_LINES))
            .andExpect(jsonPath("$.missedMethods").value(DEFAULT_MISSED_METHODS))
            .andExpect(jsonPath("$.methods").value(DEFAULT_METHODS))
            .andExpect(jsonPath("$.missedClasses").value(DEFAULT_MISSED_CLASSES))
            .andExpect(jsonPath("$.classes").value(DEFAULT_CLASSES));
    }

    @Test
    @Transactional
    void getNonExistingTestReport() throws Exception {
        // Get the testReport
        restTestReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestReport() throws Exception {
        // Initialize the database
        insertedTestReport = testReportRepository.saveAndFlush(testReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testReport
        TestReport updatedTestReport = testReportRepository.findById(testReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestReport are not directly saved in db
        em.detach(updatedTestReport);
        updatedTestReport
            .html(UPDATED_HTML)
            .runtimeRetention(UPDATED_RUNTIME_RETENTION)
            .missedInstructions(UPDATED_MISSED_INSTRUCTIONS)
            .instructions(UPDATED_INSTRUCTIONS)
            .missedBranches(UPDATED_MISSED_BRANCHES)
            .branches(UPDATED_BRANCHES)
            .missedLines(UPDATED_MISSED_LINES)
            .lines(UPDATED_LINES)
            .missedMethods(UPDATED_MISSED_METHODS)
            .methods(UPDATED_METHODS)
            .missedClasses(UPDATED_MISSED_CLASSES)
            .classes(UPDATED_CLASSES);

        restTestReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTestReport))
            )
            .andExpect(status().isOk());

        // Validate the TestReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTestReportToMatchAllProperties(updatedTestReport);
    }

    @Test
    @Transactional
    void putNonExistingTestReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testReport.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testReport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestReportWithPatch() throws Exception {
        // Initialize the database
        insertedTestReport = testReportRepository.saveAndFlush(testReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testReport using partial update
        TestReport partialUpdatedTestReport = new TestReport();
        partialUpdatedTestReport.setId(testReport.getId());

        partialUpdatedTestReport
            .html(UPDATED_HTML)
            .runtimeRetention(UPDATED_RUNTIME_RETENTION)
            .missedBranches(UPDATED_MISSED_BRANCHES)
            .lines(UPDATED_LINES);

        restTestReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestReport))
            )
            .andExpect(status().isOk());

        // Validate the TestReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTestReport, testReport),
            getPersistedTestReport(testReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateTestReportWithPatch() throws Exception {
        // Initialize the database
        insertedTestReport = testReportRepository.saveAndFlush(testReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testReport using partial update
        TestReport partialUpdatedTestReport = new TestReport();
        partialUpdatedTestReport.setId(testReport.getId());

        partialUpdatedTestReport
            .html(UPDATED_HTML)
            .runtimeRetention(UPDATED_RUNTIME_RETENTION)
            .missedInstructions(UPDATED_MISSED_INSTRUCTIONS)
            .instructions(UPDATED_INSTRUCTIONS)
            .missedBranches(UPDATED_MISSED_BRANCHES)
            .branches(UPDATED_BRANCHES)
            .missedLines(UPDATED_MISSED_LINES)
            .lines(UPDATED_LINES)
            .missedMethods(UPDATED_MISSED_METHODS)
            .methods(UPDATED_METHODS)
            .missedClasses(UPDATED_MISSED_CLASSES)
            .classes(UPDATED_CLASSES);

        restTestReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestReport))
            )
            .andExpect(status().isOk());

        // Validate the TestReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestReportUpdatableFieldsEquals(partialUpdatedTestReport, getPersistedTestReport(partialUpdatedTestReport));
    }

    @Test
    @Transactional
    void patchNonExistingTestReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(testReport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestReport() throws Exception {
        // Initialize the database
        insertedTestReport = testReportRepository.saveAndFlush(testReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the testReport
        restTestReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, testReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return testReportRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TestReport getPersistedTestReport(TestReport testReport) {
        return testReportRepository.findById(testReport.getId()).orElseThrow();
    }

    protected void assertPersistedTestReportToMatchAllProperties(TestReport expectedTestReport) {
        assertTestReportAllPropertiesEquals(expectedTestReport, getPersistedTestReport(expectedTestReport));
    }

    protected void assertPersistedTestReportToMatchUpdatableProperties(TestReport expectedTestReport) {
        assertTestReportAllUpdatablePropertiesEquals(expectedTestReport, getPersistedTestReport(expectedTestReport));
    }
}
