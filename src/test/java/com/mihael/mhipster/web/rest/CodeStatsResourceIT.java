package com.mihael.mhipster.web.rest;

import static com.mihael.mhipster.domain.CodeStatsAsserts.*;
import static com.mihael.mhipster.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mihael.mhipster.IntegrationTest;
import com.mihael.mhipster.domain.CodeStats;
import com.mihael.mhipster.repository.CodeStatsRepository;
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
 * Integration tests for the {@link CodeStatsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodeStatsResourceIT {

    private static final Double DEFAULT_INSTRUCTIONS = 1D;
    private static final Double UPDATED_INSTRUCTIONS = 2D;

    private static final Double DEFAULT_BRANCHES = 1D;
    private static final Double UPDATED_BRANCHES = 2D;

    private static final Double DEFAULT_LINES = 1D;
    private static final Double UPDATED_LINES = 2D;

    private static final Double DEFAULT_METHODS = 1D;
    private static final Double UPDATED_METHODS = 2D;

    private static final Double DEFAULT_CLASSES = 1D;
    private static final Double UPDATED_CLASSES = 2D;

    private static final Double DEFAULT_DEAD_INSTRUCTIONS = 1D;
    private static final Double UPDATED_DEAD_INSTRUCTIONS = 2D;

    private static final Double DEFAULT_DEAD_BRANCHES = 1D;
    private static final Double UPDATED_DEAD_BRANCHES = 2D;

    private static final Double DEFAULT_DEAD_LINES = 1D;
    private static final Double UPDATED_DEAD_LINES = 2D;

    private static final Double DEFAULT_DEAD_METHODS = 1D;
    private static final Double UPDATED_DEAD_METHODS = 2D;

    private static final Double DEFAULT_DEAD_CLASSES = 1D;
    private static final Double UPDATED_DEAD_CLASSES = 2D;

    private static final String ENTITY_API_URL = "/api/code-stats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CodeStatsRepository codeStatsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodeStatsMockMvc;

    private CodeStats codeStats;

    private CodeStats insertedCodeStats;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeStats createEntity() {
        return new CodeStats()
            .instructions(DEFAULT_INSTRUCTIONS)
            .branches(DEFAULT_BRANCHES)
            .lines(DEFAULT_LINES)
            .methods(DEFAULT_METHODS)
            .classes(DEFAULT_CLASSES)
            .deadInstructions(DEFAULT_DEAD_INSTRUCTIONS)
            .deadBranches(DEFAULT_DEAD_BRANCHES)
            .deadLines(DEFAULT_DEAD_LINES)
            .deadMethods(DEFAULT_DEAD_METHODS)
            .deadClasses(DEFAULT_DEAD_CLASSES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeStats createUpdatedEntity() {
        return new CodeStats()
            .instructions(UPDATED_INSTRUCTIONS)
            .branches(UPDATED_BRANCHES)
            .lines(UPDATED_LINES)
            .methods(UPDATED_METHODS)
            .classes(UPDATED_CLASSES)
            .deadInstructions(UPDATED_DEAD_INSTRUCTIONS)
            .deadBranches(UPDATED_DEAD_BRANCHES)
            .deadLines(UPDATED_DEAD_LINES)
            .deadMethods(UPDATED_DEAD_METHODS)
            .deadClasses(UPDATED_DEAD_CLASSES);
    }

    @BeforeEach
    public void initTest() {
        codeStats = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCodeStats != null) {
            codeStatsRepository.delete(insertedCodeStats);
            insertedCodeStats = null;
        }
    }

    @Test
    @Transactional
    void createCodeStats() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CodeStats
        var returnedCodeStats = om.readValue(
            restCodeStatsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeStats)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CodeStats.class
        );

        // Validate the CodeStats in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCodeStatsUpdatableFieldsEquals(returnedCodeStats, getPersistedCodeStats(returnedCodeStats));

        insertedCodeStats = returnedCodeStats;
    }

    @Test
    @Transactional
    void createCodeStatsWithExistingId() throws Exception {
        // Create the CodeStats with an existing ID
        codeStats.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeStatsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeStats)))
            .andExpect(status().isBadRequest());

        // Validate the CodeStats in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCodeStats() throws Exception {
        // Initialize the database
        insertedCodeStats = codeStatsRepository.saveAndFlush(codeStats);

        // Get all the codeStatsList
        restCodeStatsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeStats.getId().intValue())))
            .andExpect(jsonPath("$.[*].instructions").value(hasItem(DEFAULT_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].branches").value(hasItem(DEFAULT_BRANCHES)))
            .andExpect(jsonPath("$.[*].lines").value(hasItem(DEFAULT_LINES)))
            .andExpect(jsonPath("$.[*].methods").value(hasItem(DEFAULT_METHODS)))
            .andExpect(jsonPath("$.[*].classes").value(hasItem(DEFAULT_CLASSES)))
            .andExpect(jsonPath("$.[*].deadInstructions").value(hasItem(DEFAULT_DEAD_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].deadBranches").value(hasItem(DEFAULT_DEAD_BRANCHES)))
            .andExpect(jsonPath("$.[*].deadLines").value(hasItem(DEFAULT_DEAD_LINES)))
            .andExpect(jsonPath("$.[*].deadMethods").value(hasItem(DEFAULT_DEAD_METHODS)))
            .andExpect(jsonPath("$.[*].deadClasses").value(hasItem(DEFAULT_DEAD_CLASSES)));
    }

    @Test
    @Transactional
    void getCodeStats() throws Exception {
        // Initialize the database
        insertedCodeStats = codeStatsRepository.saveAndFlush(codeStats);

        // Get the codeStats
        restCodeStatsMockMvc
            .perform(get(ENTITY_API_URL_ID, codeStats.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codeStats.getId().intValue()))
            .andExpect(jsonPath("$.instructions").value(DEFAULT_INSTRUCTIONS))
            .andExpect(jsonPath("$.branches").value(DEFAULT_BRANCHES))
            .andExpect(jsonPath("$.lines").value(DEFAULT_LINES))
            .andExpect(jsonPath("$.methods").value(DEFAULT_METHODS))
            .andExpect(jsonPath("$.classes").value(DEFAULT_CLASSES))
            .andExpect(jsonPath("$.deadInstructions").value(DEFAULT_DEAD_INSTRUCTIONS))
            .andExpect(jsonPath("$.deadBranches").value(DEFAULT_DEAD_BRANCHES))
            .andExpect(jsonPath("$.deadLines").value(DEFAULT_DEAD_LINES))
            .andExpect(jsonPath("$.deadMethods").value(DEFAULT_DEAD_METHODS))
            .andExpect(jsonPath("$.deadClasses").value(DEFAULT_DEAD_CLASSES));
    }

    @Test
    @Transactional
    void getNonExistingCodeStats() throws Exception {
        // Get the codeStats
        restCodeStatsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCodeStats() throws Exception {
        // Initialize the database
        insertedCodeStats = codeStatsRepository.saveAndFlush(codeStats);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codeStats
        CodeStats updatedCodeStats = codeStatsRepository.findById(codeStats.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCodeStats are not directly saved in db
        em.detach(updatedCodeStats);
        updatedCodeStats
            .instructions(UPDATED_INSTRUCTIONS)
            .branches(UPDATED_BRANCHES)
            .lines(UPDATED_LINES)
            .methods(UPDATED_METHODS)
            .classes(UPDATED_CLASSES)
            .deadInstructions(UPDATED_DEAD_INSTRUCTIONS)
            .deadBranches(UPDATED_DEAD_BRANCHES)
            .deadLines(UPDATED_DEAD_LINES)
            .deadMethods(UPDATED_DEAD_METHODS)
            .deadClasses(UPDATED_DEAD_CLASSES);

        restCodeStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCodeStats.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCodeStats))
            )
            .andExpect(status().isOk());

        // Validate the CodeStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCodeStatsToMatchAllProperties(updatedCodeStats);
    }

    @Test
    @Transactional
    void putNonExistingCodeStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeStats.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeStats.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeStats))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodeStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeStats.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codeStats))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodeStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeStats.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeStatsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeStats)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodeStatsWithPatch() throws Exception {
        // Initialize the database
        insertedCodeStats = codeStatsRepository.saveAndFlush(codeStats);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codeStats using partial update
        CodeStats partialUpdatedCodeStats = new CodeStats();
        partialUpdatedCodeStats.setId(codeStats.getId());

        partialUpdatedCodeStats
            .instructions(UPDATED_INSTRUCTIONS)
            .branches(UPDATED_BRANCHES)
            .methods(UPDATED_METHODS)
            .deadInstructions(UPDATED_DEAD_INSTRUCTIONS)
            .deadBranches(UPDATED_DEAD_BRANCHES)
            .deadLines(UPDATED_DEAD_LINES)
            .deadClasses(UPDATED_DEAD_CLASSES);

        restCodeStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeStats.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodeStats))
            )
            .andExpect(status().isOk());

        // Validate the CodeStats in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodeStatsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCodeStats, codeStats),
            getPersistedCodeStats(codeStats)
        );
    }

    @Test
    @Transactional
    void fullUpdateCodeStatsWithPatch() throws Exception {
        // Initialize the database
        insertedCodeStats = codeStatsRepository.saveAndFlush(codeStats);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codeStats using partial update
        CodeStats partialUpdatedCodeStats = new CodeStats();
        partialUpdatedCodeStats.setId(codeStats.getId());

        partialUpdatedCodeStats
            .instructions(UPDATED_INSTRUCTIONS)
            .branches(UPDATED_BRANCHES)
            .lines(UPDATED_LINES)
            .methods(UPDATED_METHODS)
            .classes(UPDATED_CLASSES)
            .deadInstructions(UPDATED_DEAD_INSTRUCTIONS)
            .deadBranches(UPDATED_DEAD_BRANCHES)
            .deadLines(UPDATED_DEAD_LINES)
            .deadMethods(UPDATED_DEAD_METHODS)
            .deadClasses(UPDATED_DEAD_CLASSES);

        restCodeStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeStats.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodeStats))
            )
            .andExpect(status().isOk());

        // Validate the CodeStats in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodeStatsUpdatableFieldsEquals(partialUpdatedCodeStats, getPersistedCodeStats(partialUpdatedCodeStats));
    }

    @Test
    @Transactional
    void patchNonExistingCodeStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeStats.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codeStats.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codeStats))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodeStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeStats.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codeStats))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodeStats() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeStats.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeStatsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(codeStats)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeStats in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodeStats() throws Exception {
        // Initialize the database
        insertedCodeStats = codeStatsRepository.saveAndFlush(codeStats);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the codeStats
        restCodeStatsMockMvc
            .perform(delete(ENTITY_API_URL_ID, codeStats.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return codeStatsRepository.count();
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

    protected CodeStats getPersistedCodeStats(CodeStats codeStats) {
        return codeStatsRepository.findById(codeStats.getId()).orElseThrow();
    }

    protected void assertPersistedCodeStatsToMatchAllProperties(CodeStats expectedCodeStats) {
        assertCodeStatsAllPropertiesEquals(expectedCodeStats, getPersistedCodeStats(expectedCodeStats));
    }

    protected void assertPersistedCodeStatsToMatchUpdatableProperties(CodeStats expectedCodeStats) {
        assertCodeStatsAllUpdatablePropertiesEquals(expectedCodeStats, getPersistedCodeStats(expectedCodeStats));
    }
}
