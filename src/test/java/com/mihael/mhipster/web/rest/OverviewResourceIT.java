package com.mihael.mhipster.web.rest;

import static com.mihael.mhipster.domain.OverviewAsserts.*;
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
import com.mihael.mhipster.domain.Overview;
import com.mihael.mhipster.domain.User;
import com.mihael.mhipster.repository.OverviewRepository;
import com.mihael.mhipster.repository.UserRepository;
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
 * Integration tests for the {@link OverviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OverviewResourceIT {

    private static final ZonedDateTime DEFAULT_DATE_GENERATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_GENERATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/overviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OverviewRepository overviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private OverviewRepository overviewRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOverviewMockMvc;

    private Overview overview;

    private Overview insertedOverview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Overview createEntity(EntityManager em) {
        Overview overview = new Overview().dateGenerated(DEFAULT_DATE_GENERATED);
        // Add required entity
        CodeStats codeStats;
        if (TestUtil.findAll(em, CodeStats.class).isEmpty()) {
            codeStats = CodeStatsResourceIT.createEntity();
            em.persist(codeStats);
            em.flush();
        } else {
            codeStats = TestUtil.findAll(em, CodeStats.class).get(0);
        }
        overview.setParent(codeStats);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        overview.setUser(user);
        return overview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Overview createUpdatedEntity(EntityManager em) {
        Overview updatedOverview = new Overview().dateGenerated(UPDATED_DATE_GENERATED);
        // Add required entity
        CodeStats codeStats;
        if (TestUtil.findAll(em, CodeStats.class).isEmpty()) {
            codeStats = CodeStatsResourceIT.createUpdatedEntity();
            em.persist(codeStats);
            em.flush();
        } else {
            codeStats = TestUtil.findAll(em, CodeStats.class).get(0);
        }
        updatedOverview.setParent(codeStats);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedOverview.setUser(user);
        return updatedOverview;
    }

    @BeforeEach
    public void initTest() {
        overview = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedOverview != null) {
            overviewRepository.delete(insertedOverview);
            insertedOverview = null;
        }
    }

    @Test
    @Transactional
    void createOverview() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Overview
        var returnedOverview = om.readValue(
            restOverviewMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(overview)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Overview.class
        );

        // Validate the Overview in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOverviewUpdatableFieldsEquals(returnedOverview, getPersistedOverview(returnedOverview));

        insertedOverview = returnedOverview;
    }

    @Test
    @Transactional
    void createOverviewWithExistingId() throws Exception {
        // Create the Overview with an existing ID
        overview.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOverviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(overview)))
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOverviews() throws Exception {
        // Initialize the database
        insertedOverview = overviewRepository.saveAndFlush(overview);

        // Get all the overviewList
        restOverviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(overview.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateGenerated").value(hasItem(sameInstant(DEFAULT_DATE_GENERATED))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOverviewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(overviewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOverviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(overviewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOverviewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(overviewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOverviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(overviewRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOverview() throws Exception {
        // Initialize the database
        insertedOverview = overviewRepository.saveAndFlush(overview);

        // Get the overview
        restOverviewMockMvc
            .perform(get(ENTITY_API_URL_ID, overview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(overview.getId().intValue()))
            .andExpect(jsonPath("$.dateGenerated").value(sameInstant(DEFAULT_DATE_GENERATED)));
    }

    @Test
    @Transactional
    void getNonExistingOverview() throws Exception {
        // Get the overview
        restOverviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOverview() throws Exception {
        // Initialize the database
        insertedOverview = overviewRepository.saveAndFlush(overview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the overview
        Overview updatedOverview = overviewRepository.findById(overview.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOverview are not directly saved in db
        em.detach(updatedOverview);
        updatedOverview.dateGenerated(UPDATED_DATE_GENERATED);

        restOverviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOverview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOverview))
            )
            .andExpect(status().isOk());

        // Validate the Overview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOverviewToMatchAllProperties(updatedOverview);
    }

    @Test
    @Transactional
    void putNonExistingOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        overview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, overview.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(overview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        overview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(overview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        overview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(overview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Overview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOverviewWithPatch() throws Exception {
        // Initialize the database
        insertedOverview = overviewRepository.saveAndFlush(overview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the overview using partial update
        Overview partialUpdatedOverview = new Overview();
        partialUpdatedOverview.setId(overview.getId());

        partialUpdatedOverview.dateGenerated(UPDATED_DATE_GENERATED);

        restOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOverview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOverview))
            )
            .andExpect(status().isOk());

        // Validate the Overview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOverviewUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOverview, overview), getPersistedOverview(overview));
    }

    @Test
    @Transactional
    void fullUpdateOverviewWithPatch() throws Exception {
        // Initialize the database
        insertedOverview = overviewRepository.saveAndFlush(overview);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the overview using partial update
        Overview partialUpdatedOverview = new Overview();
        partialUpdatedOverview.setId(overview.getId());

        partialUpdatedOverview.dateGenerated(UPDATED_DATE_GENERATED);

        restOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOverview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOverview))
            )
            .andExpect(status().isOk());

        // Validate the Overview in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOverviewUpdatableFieldsEquals(partialUpdatedOverview, getPersistedOverview(partialUpdatedOverview));
    }

    @Test
    @Transactional
    void patchNonExistingOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        overview.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, overview.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(overview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        overview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(overview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOverview() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        overview.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(overview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Overview in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOverview() throws Exception {
        // Initialize the database
        insertedOverview = overviewRepository.saveAndFlush(overview);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the overview
        restOverviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, overview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return overviewRepository.count();
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

    protected Overview getPersistedOverview(Overview overview) {
        return overviewRepository.findById(overview.getId()).orElseThrow();
    }

    protected void assertPersistedOverviewToMatchAllProperties(Overview expectedOverview) {
        assertOverviewAllPropertiesEquals(expectedOverview, getPersistedOverview(expectedOverview));
    }

    protected void assertPersistedOverviewToMatchUpdatableProperties(Overview expectedOverview) {
        assertOverviewAllUpdatablePropertiesEquals(expectedOverview, getPersistedOverview(expectedOverview));
    }
}
