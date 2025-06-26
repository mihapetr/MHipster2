package com.mihael.mhipster.web.rest;

import static com.mihael.mhipster.domain.FeatureTstAsserts.*;
import static com.mihael.mhipster.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mihael.mhipster.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mihael.mhipster.IntegrationTest;
import com.mihael.mhipster.domain.CodeStats;
import com.mihael.mhipster.domain.FeatureTst;
import com.mihael.mhipster.domain.Project;
import com.mihael.mhipster.repository.FeatureTstRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FeatureTstResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FeatureTstResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/feature-tsts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeatureTstRepository featureTstRepository;

    @Mock
    private FeatureTstRepository featureTstRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeatureTstMockMvc;

    private FeatureTst featureTst;

    private FeatureTst insertedFeatureTst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeatureTst createEntity(EntityManager em) {
        FeatureTst featureTst = new FeatureTst().date(DEFAULT_DATE);
        // Add required entity
        CodeStats codeStats;
        if (TestUtil.findAll(em, CodeStats.class).isEmpty()) {
            codeStats = CodeStatsResourceIT.createEntity();
            em.persist(codeStats);
            em.flush();
        } else {
            codeStats = TestUtil.findAll(em, CodeStats.class).get(0);
        }
        featureTst.setParent(codeStats);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        featureTst.setProject(project);
        return featureTst;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeatureTst createUpdatedEntity(EntityManager em) {
        FeatureTst updatedFeatureTst = new FeatureTst().date(UPDATED_DATE);
        // Add required entity
        CodeStats codeStats;
        if (TestUtil.findAll(em, CodeStats.class).isEmpty()) {
            codeStats = CodeStatsResourceIT.createUpdatedEntity();
            em.persist(codeStats);
            em.flush();
        } else {
            codeStats = TestUtil.findAll(em, CodeStats.class).get(0);
        }
        updatedFeatureTst.setParent(codeStats);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        updatedFeatureTst.setProject(project);
        return updatedFeatureTst;
    }

    @BeforeEach
    public void initTest() {
        featureTst = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeatureTst != null) {
            featureTstRepository.delete(insertedFeatureTst);
            insertedFeatureTst = null;
        }
    }

    @Test
    @Transactional
    void createFeatureTst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeatureTst
        var returnedFeatureTst = om.readValue(
            restFeatureTstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureTst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeatureTst.class
        );

        // Validate the FeatureTst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeatureTstUpdatableFieldsEquals(returnedFeatureTst, getPersistedFeatureTst(returnedFeatureTst));

        insertedFeatureTst = returnedFeatureTst;
    }

    @Test
    @Transactional
    void createFeatureTstWithExistingId() throws Exception {
        // Create the FeatureTst with an existing ID
        featureTst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeatureTstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureTst)))
            .andExpect(status().isBadRequest());

        // Validate the FeatureTst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeatureTsts() throws Exception {
        // Initialize the database
        insertedFeatureTst = featureTstRepository.saveAndFlush(featureTst);

        // Get all the featureTstList
        restFeatureTstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(featureTst.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeatureTstsWithEagerRelationshipsIsEnabled() throws Exception {
        when(featureTstRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeatureTstMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(featureTstRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeatureTstsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(featureTstRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeatureTstMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(featureTstRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFeatureTst() throws Exception {
        // Initialize the database
        insertedFeatureTst = featureTstRepository.saveAndFlush(featureTst);

        // Get the featureTst
        restFeatureTstMockMvc
            .perform(get(ENTITY_API_URL_ID, featureTst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(featureTst.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingFeatureTst() throws Exception {
        // Get the featureTst
        restFeatureTstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeatureTst() throws Exception {
        // Initialize the database
        insertedFeatureTst = featureTstRepository.saveAndFlush(featureTst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the featureTst
        FeatureTst updatedFeatureTst = featureTstRepository.findById(featureTst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeatureTst are not directly saved in db
        em.detach(updatedFeatureTst);
        updatedFeatureTst.date(UPDATED_DATE);

        restFeatureTstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeatureTst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeatureTst))
            )
            .andExpect(status().isOk());

        // Validate the FeatureTst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeatureTstToMatchAllProperties(updatedFeatureTst);
    }

    @Test
    @Transactional
    void putNonExistingFeatureTst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureTst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeatureTstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, featureTst.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureTst))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureTst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeatureTst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureTst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureTstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(featureTst))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureTst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeatureTst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureTst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureTstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureTst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeatureTst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeatureTstWithPatch() throws Exception {
        // Initialize the database
        insertedFeatureTst = featureTstRepository.saveAndFlush(featureTst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the featureTst using partial update
        FeatureTst partialUpdatedFeatureTst = new FeatureTst();
        partialUpdatedFeatureTst.setId(featureTst.getId());

        partialUpdatedFeatureTst.date(UPDATED_DATE);

        restFeatureTstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeatureTst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeatureTst))
            )
            .andExpect(status().isOk());

        // Validate the FeatureTst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureTstUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeatureTst, featureTst),
            getPersistedFeatureTst(featureTst)
        );
    }

    @Test
    @Transactional
    void fullUpdateFeatureTstWithPatch() throws Exception {
        // Initialize the database
        insertedFeatureTst = featureTstRepository.saveAndFlush(featureTst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the featureTst using partial update
        FeatureTst partialUpdatedFeatureTst = new FeatureTst();
        partialUpdatedFeatureTst.setId(featureTst.getId());

        partialUpdatedFeatureTst.date(UPDATED_DATE);

        restFeatureTstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeatureTst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeatureTst))
            )
            .andExpect(status().isOk());

        // Validate the FeatureTst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureTstUpdatableFieldsEquals(partialUpdatedFeatureTst, getPersistedFeatureTst(partialUpdatedFeatureTst));
    }

    @Test
    @Transactional
    void patchNonExistingFeatureTst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureTst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeatureTstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, featureTst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(featureTst))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureTst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeatureTst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureTst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureTstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(featureTst))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureTst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeatureTst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureTst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureTstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(featureTst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeatureTst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeatureTst() throws Exception {
        // Initialize the database
        insertedFeatureTst = featureTstRepository.saveAndFlush(featureTst);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the featureTst
        restFeatureTstMockMvc
            .perform(delete(ENTITY_API_URL_ID, featureTst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return featureTstRepository.count();
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

    protected FeatureTst getPersistedFeatureTst(FeatureTst featureTst) {
        return featureTstRepository.findById(featureTst.getId()).orElseThrow();
    }

    protected void assertPersistedFeatureTstToMatchAllProperties(FeatureTst expectedFeatureTst) {
        assertFeatureTstAllPropertiesEquals(expectedFeatureTst, getPersistedFeatureTst(expectedFeatureTst));
    }

    protected void assertPersistedFeatureTstToMatchUpdatableProperties(FeatureTst expectedFeatureTst) {
        assertFeatureTstAllUpdatablePropertiesEquals(expectedFeatureTst, getPersistedFeatureTst(expectedFeatureTst));
    }
}
