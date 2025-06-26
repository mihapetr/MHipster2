package com.mihael.mhipster.web.rest;

import static com.mihael.mhipster.domain.FeatureAsserts.*;
import static com.mihael.mhipster.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mihael.mhipster.IntegrationTest;
import com.mihael.mhipster.domain.Feature;
import com.mihael.mhipster.domain.User;
import com.mihael.mhipster.repository.FeatureRepository;
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
 * Integration tests for the {@link FeatureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeatureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/features";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeatureMockMvc;

    private Feature feature;

    private Feature insertedFeature;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feature createEntity(EntityManager em) {
        Feature feature = new Feature().name(DEFAULT_NAME).content(DEFAULT_CONTENT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        feature.setUser(user);
        return feature;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feature createUpdatedEntity(EntityManager em) {
        Feature updatedFeature = new Feature().name(UPDATED_NAME).content(UPDATED_CONTENT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedFeature.setUser(user);
        return updatedFeature;
    }

    @BeforeEach
    public void initTest() {
        feature = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeature != null) {
            featureRepository.delete(insertedFeature);
            insertedFeature = null;
        }
    }

    @Test
    @Transactional
    void createFeature() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Feature
        var returnedFeature = om.readValue(
            restFeatureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feature)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Feature.class
        );

        // Validate the Feature in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeatureUpdatableFieldsEquals(returnedFeature, getPersistedFeature(returnedFeature));

        insertedFeature = returnedFeature;
    }

    @Test
    @Transactional
    void createFeatureWithExistingId() throws Exception {
        // Create the Feature with an existing ID
        feature.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeatureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feature)))
            .andExpect(status().isBadRequest());

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeatures() throws Exception {
        // Initialize the database
        insertedFeature = featureRepository.saveAndFlush(feature);

        // Get all the featureList
        restFeatureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feature.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getFeature() throws Exception {
        // Initialize the database
        insertedFeature = featureRepository.saveAndFlush(feature);

        // Get the feature
        restFeatureMockMvc
            .perform(get(ENTITY_API_URL_ID, feature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feature.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingFeature() throws Exception {
        // Get the feature
        restFeatureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeature() throws Exception {
        // Initialize the database
        insertedFeature = featureRepository.saveAndFlush(feature);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feature
        Feature updatedFeature = featureRepository.findById(feature.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeature are not directly saved in db
        em.detach(updatedFeature);
        updatedFeature.name(UPDATED_NAME).content(UPDATED_CONTENT);

        restFeatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeature.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeature))
            )
            .andExpect(status().isOk());

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeatureToMatchAllProperties(updatedFeature);
    }

    @Test
    @Transactional
    void putNonExistingFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeatureMockMvc
            .perform(put(ENTITY_API_URL_ID, feature.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feature)))
            .andExpect(status().isBadRequest());

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feature))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feature)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeatureWithPatch() throws Exception {
        // Initialize the database
        insertedFeature = featureRepository.saveAndFlush(feature);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feature using partial update
        Feature partialUpdatedFeature = new Feature();
        partialUpdatedFeature.setId(feature.getId());

        restFeatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeature.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeature))
            )
            .andExpect(status().isOk());

        // Validate the Feature in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFeature, feature), getPersistedFeature(feature));
    }

    @Test
    @Transactional
    void fullUpdateFeatureWithPatch() throws Exception {
        // Initialize the database
        insertedFeature = featureRepository.saveAndFlush(feature);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feature using partial update
        Feature partialUpdatedFeature = new Feature();
        partialUpdatedFeature.setId(feature.getId());

        partialUpdatedFeature.name(UPDATED_NAME).content(UPDATED_CONTENT);

        restFeatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeature.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeature))
            )
            .andExpect(status().isOk());

        // Validate the Feature in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureUpdatableFieldsEquals(partialUpdatedFeature, getPersistedFeature(partialUpdatedFeature));
    }

    @Test
    @Transactional
    void patchNonExistingFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feature.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feature))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feature))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeature() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feature.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feature)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feature in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeature() throws Exception {
        // Initialize the database
        insertedFeature = featureRepository.saveAndFlush(feature);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feature
        restFeatureMockMvc
            .perform(delete(ENTITY_API_URL_ID, feature.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return featureRepository.count();
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

    protected Feature getPersistedFeature(Feature feature) {
        return featureRepository.findById(feature.getId()).orElseThrow();
    }

    protected void assertPersistedFeatureToMatchAllProperties(Feature expectedFeature) {
        assertFeatureAllPropertiesEquals(expectedFeature, getPersistedFeature(expectedFeature));
    }

    protected void assertPersistedFeatureToMatchUpdatableProperties(Feature expectedFeature) {
        assertFeatureAllUpdatablePropertiesEquals(expectedFeature, getPersistedFeature(expectedFeature));
    }
}
