package com.mihael.mhipster.web.rest;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.Feature;
import com.mihael.mhipster.repository.FeatureRepository;
import com.mihael.mhipster.repository.UserRepository;
import com.mihael.mhipster.security.SecurityUtils;
import com.mihael.mhipster.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link com.mihael.mhipster.domain.Feature}.
 */
@RestController
@RequestMapping("/api/features")
@Transactional
public class FeatureResource {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureResource.class);

    private static final String ENTITY_NAME = "feature";
    private final UserRepository userRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeatureRepository featureRepository;

    public FeatureResource(FeatureRepository featureRepository, UserRepository userRepository) {
        this.featureRepository = featureRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /features} : Create a new feature.
     *
     * @param feature the feature to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feature, or with status {@code 400 (Bad Request)} if the feature has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Feature> createFeature(@Valid @RequestBody Feature feature) throws URISyntaxException {
        LOG.debug("REST request to save Feature : {}", feature);
        if (feature.getId() != null) {
            throw new BadRequestAlertException("A new feature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        createFeatureCustom(feature);
        feature = featureRepository.save(feature);
        return ResponseEntity.created(new URI("/api/features/" + feature.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, feature.getId().toString()))
            .body(feature);
    }

    @MGenerated
    void createFeatureCustom(Feature feature) {
        feature.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());
    }

    /**
     * {@code PUT  /features/:id} : Updates an existing feature.
     *
     * @param id the id of the feature to save.
     * @param feature the feature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feature,
     * or with status {@code 400 (Bad Request)} if the feature is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Feature> updateFeature(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Feature feature
    ) throws URISyntaxException {
        LOG.debug("REST request to update Feature : {}, {}", id, feature);
        if (feature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feature = featureRepository.save(feature);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, feature.getId().toString()))
            .body(feature);
    }

    /**
     * {@code PATCH  /features/:id} : Partial updates given fields of an existing feature, field will ignore if it is null
     *
     * @param id the id of the feature to save.
     * @param feature the feature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feature,
     * or with status {@code 400 (Bad Request)} if the feature is not valid,
     * or with status {@code 404 (Not Found)} if the feature is not found,
     * or with status {@code 500 (Internal Server Error)} if the feature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Feature> partialUpdateFeature(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Feature feature
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Feature partially : {}, {}", id, feature);
        if (feature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Feature> result = featureRepository
            .findById(feature.getId())
            .map(existingFeature -> {
                if (feature.getName() != null) {
                    existingFeature.setName(feature.getName());
                }
                if (feature.getContent() != null) {
                    existingFeature.setContent(feature.getContent());
                }

                return existingFeature;
            })
            .map(featureRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, feature.getId().toString())
        );
    }

    /**
     * {@code GET  /features} : get all the features.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of features in body.
     */
    @GetMapping("")
    public List<Feature> getAllFeatures(@RequestParam(name = "filter", required = false) String filter) {
        LOG.debug("REST request to get all Features");
        if (filter != null) return filter(filter);
        return featureRepository.findAll();
    }

    @MGenerated
    List<Feature> filter(String filter) {
        if (filter.equals("current-user")) {
            return featureRepository.findByUserIsCurrentUser();
        } else throw new BadRequestAlertException("Invalid filter", ENTITY_NAME, "invalidfilter");
    }

    /**
     * {@code GET  /features/:id} : get the "id" feature.
     *
     * @param id the id of the feature to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feature, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Feature> getFeature(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Feature : {}", id);
        Optional<Feature> feature = featureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(feature);
    }

    /**
     * {@code DELETE  /features/:id} : delete the "id" feature.
     *
     * @param id the id of the feature to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Feature : {}", id);
        featureRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
