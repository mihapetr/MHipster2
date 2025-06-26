package com.mihael.mhipster.web.rest;

import static com.mihael.mhipster.domain.MDLSAsserts.*;
import static com.mihael.mhipster.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mihael.mhipster.IntegrationTest;
import com.mihael.mhipster.domain.MDLS;
import com.mihael.mhipster.domain.User;
import com.mihael.mhipster.repository.MDLSRepository;
import com.mihael.mhipster.repository.UserRepository;
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
 * Integration tests for the {@link MDLSResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MDLSResourceIT {

    private static final String DEFAULT_BASE_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_BASE_CONFIG = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mdls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MDLSRepository mDLSRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMDLSMockMvc;

    private MDLS mDLS;

    private MDLS insertedMDLS;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MDLS createEntity(EntityManager em) {
        MDLS mDLS = new MDLS().baseConfig(DEFAULT_BASE_CONFIG).content(DEFAULT_CONTENT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        mDLS.setUser(user);
        return mDLS;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MDLS createUpdatedEntity(EntityManager em) {
        MDLS updatedMDLS = new MDLS().baseConfig(UPDATED_BASE_CONFIG).content(UPDATED_CONTENT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedMDLS.setUser(user);
        return updatedMDLS;
    }

    @BeforeEach
    public void initTest() {
        mDLS = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMDLS != null) {
            mDLSRepository.delete(insertedMDLS);
            insertedMDLS = null;
        }
    }

    @Test
    @Transactional
    void createMDLS() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MDLS
        var returnedMDLS = om.readValue(
            restMDLSMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mDLS)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MDLS.class
        );

        // Validate the MDLS in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMDLSUpdatableFieldsEquals(returnedMDLS, getPersistedMDLS(returnedMDLS));

        insertedMDLS = returnedMDLS;
    }

    @Test
    @Transactional
    void createMDLSWithExistingId() throws Exception {
        // Create the MDLS with an existing ID
        mDLS.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMDLSMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mDLS)))
            .andExpect(status().isBadRequest());

        // Validate the MDLS in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMDLS() throws Exception {
        // Initialize the database
        insertedMDLS = mDLSRepository.saveAndFlush(mDLS);

        // Get all the mDLSList
        restMDLSMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mDLS.getId().intValue())))
            .andExpect(jsonPath("$.[*].baseConfig").value(hasItem(DEFAULT_BASE_CONFIG)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getMDLS() throws Exception {
        // Initialize the database
        insertedMDLS = mDLSRepository.saveAndFlush(mDLS);

        // Get the mDLS
        restMDLSMockMvc
            .perform(get(ENTITY_API_URL_ID, mDLS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mDLS.getId().intValue()))
            .andExpect(jsonPath("$.baseConfig").value(DEFAULT_BASE_CONFIG))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingMDLS() throws Exception {
        // Get the mDLS
        restMDLSMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMDLS() throws Exception {
        // Initialize the database
        insertedMDLS = mDLSRepository.saveAndFlush(mDLS);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mDLS
        MDLS updatedMDLS = mDLSRepository.findById(mDLS.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMDLS are not directly saved in db
        em.detach(updatedMDLS);
        updatedMDLS.baseConfig(UPDATED_BASE_CONFIG).content(UPDATED_CONTENT);

        restMDLSMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMDLS.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMDLS))
            )
            .andExpect(status().isOk());

        // Validate the MDLS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMDLSToMatchAllProperties(updatedMDLS);
    }

    @Test
    @Transactional
    void putNonExistingMDLS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mDLS.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMDLSMockMvc
            .perform(put(ENTITY_API_URL_ID, mDLS.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mDLS)))
            .andExpect(status().isBadRequest());

        // Validate the MDLS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMDLS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mDLS.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMDLSMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mDLS))
            )
            .andExpect(status().isBadRequest());

        // Validate the MDLS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMDLS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mDLS.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMDLSMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mDLS)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MDLS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMDLSWithPatch() throws Exception {
        // Initialize the database
        insertedMDLS = mDLSRepository.saveAndFlush(mDLS);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mDLS using partial update
        MDLS partialUpdatedMDLS = new MDLS();
        partialUpdatedMDLS.setId(mDLS.getId());

        restMDLSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMDLS.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMDLS))
            )
            .andExpect(status().isOk());

        // Validate the MDLS in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMDLSUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMDLS, mDLS), getPersistedMDLS(mDLS));
    }

    @Test
    @Transactional
    void fullUpdateMDLSWithPatch() throws Exception {
        // Initialize the database
        insertedMDLS = mDLSRepository.saveAndFlush(mDLS);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mDLS using partial update
        MDLS partialUpdatedMDLS = new MDLS();
        partialUpdatedMDLS.setId(mDLS.getId());

        partialUpdatedMDLS.baseConfig(UPDATED_BASE_CONFIG).content(UPDATED_CONTENT);

        restMDLSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMDLS.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMDLS))
            )
            .andExpect(status().isOk());

        // Validate the MDLS in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMDLSUpdatableFieldsEquals(partialUpdatedMDLS, getPersistedMDLS(partialUpdatedMDLS));
    }

    @Test
    @Transactional
    void patchNonExistingMDLS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mDLS.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMDLSMockMvc
            .perform(patch(ENTITY_API_URL_ID, mDLS.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mDLS)))
            .andExpect(status().isBadRequest());

        // Validate the MDLS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMDLS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mDLS.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMDLSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mDLS))
            )
            .andExpect(status().isBadRequest());

        // Validate the MDLS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMDLS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mDLS.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMDLSMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mDLS)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MDLS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMDLS() throws Exception {
        // Initialize the database
        insertedMDLS = mDLSRepository.saveAndFlush(mDLS);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mDLS
        restMDLSMockMvc
            .perform(delete(ENTITY_API_URL_ID, mDLS.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mDLSRepository.count();
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

    protected MDLS getPersistedMDLS(MDLS mDLS) {
        return mDLSRepository.findById(mDLS.getId()).orElseThrow();
    }

    protected void assertPersistedMDLSToMatchAllProperties(MDLS expectedMDLS) {
        assertMDLSAllPropertiesEquals(expectedMDLS, getPersistedMDLS(expectedMDLS));
    }

    protected void assertPersistedMDLSToMatchUpdatableProperties(MDLS expectedMDLS) {
        assertMDLSAllUpdatablePropertiesEquals(expectedMDLS, getPersistedMDLS(expectedMDLS));
    }
}
