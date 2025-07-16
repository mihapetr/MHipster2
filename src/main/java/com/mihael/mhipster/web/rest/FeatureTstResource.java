package com.mihael.mhipster.web.rest;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.FeatureTst;
import com.mihael.mhipster.domain.Project;
import com.mihael.mhipster.repository.FeatureTstRepository;
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
 * REST controller for managing {@link com.mihael.mhipster.domain.FeatureTst}.
 */
@RestController
@RequestMapping("/api/feature-tsts")
@Transactional
public class FeatureTstResource {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureTstResource.class);

    private static final String ENTITY_NAME = "featureTst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeatureTstRepository featureTstRepository;

    public FeatureTstResource(FeatureTstRepository featureTstRepository) {
        this.featureTstRepository = featureTstRepository;
    }

    /**
     * {@code POST  /feature-tsts} : Create a new featureTst.
     *
     * @param featureTst the featureTst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new featureTst, or with status {@code 400 (Bad Request)} if the featureTst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeatureTst> createFeatureTst(@Valid @RequestBody FeatureTst featureTst) throws URISyntaxException {
        LOG.debug("REST request to save FeatureTst : {}", featureTst);
        if (featureTst.getId() != null) {
            throw new BadRequestAlertException("A new featureTst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        featureTst = featureTstRepository.save(featureTst);
        return ResponseEntity.created(new URI("/api/feature-tsts/" + featureTst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, featureTst.getId().toString()))
            .body(featureTst);
    }

    /**
     * {@code PUT  /feature-tsts/:id} : Updates an existing featureTst.
     *
     * @param id the id of the featureTst to save.
     * @param featureTst the featureTst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featureTst,
     * or with status {@code 400 (Bad Request)} if the featureTst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the featureTst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeatureTst> updateFeatureTst(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeatureTst featureTst
    ) throws URISyntaxException {
        LOG.debug("REST request to update FeatureTst : {}, {}", id, featureTst);
        if (featureTst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featureTst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featureTstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        featureTst = featureTstRepository.save(featureTst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, featureTst.getId().toString()))
            .body(featureTst);
    }

    /**
     * {@code PATCH  /feature-tsts/:id} : Partial updates given fields of an existing featureTst, field will ignore if it is null
     *
     * @param id the id of the featureTst to save.
     * @param featureTst the featureTst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated featureTst,
     * or with status {@code 400 (Bad Request)} if the featureTst is not valid,
     * or with status {@code 404 (Not Found)} if the featureTst is not found,
     * or with status {@code 500 (Internal Server Error)} if the featureTst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeatureTst> partialUpdateFeatureTst(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeatureTst featureTst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FeatureTst partially : {}, {}", id, featureTst);
        if (featureTst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, featureTst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!featureTstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeatureTst> result = featureTstRepository
            .findById(featureTst.getId())
            .map(existingFeatureTst -> {
                if (featureTst.getDate() != null) {
                    existingFeatureTst.setDate(featureTst.getDate());
                }

                return existingFeatureTst;
            })
            .map(featureTstRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, featureTst.getId().toString())
        );
    }

    /**
     * {@code GET  /feature-tsts} : get all the featureTsts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of featureTsts in body.
     */
    @GetMapping("")
    public List<FeatureTst> getAllFeatureTsts(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        LOG.debug("REST request to get all FeatureTsts");
        if (eagerload) {
            if (filter != null) return filter(filter);
            return featureTstRepository.findAllWithEagerRelationships();
        } else {
            if (filter != null) return filter(filter);
            return featureTstRepository.findAll();
        }
    }

    @MGenerated
    List<FeatureTst> filter(String filter) {
        if (filter.equals("current-user")) {
            return featureTstRepository.findAll(); // todo : implement this
        } else return featureTstRepository.findAll();
    }

    /**
     * {@code GET  /feature-tsts/:id} : get the "id" featureTst.
     *
     * @param id the id of the featureTst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the featureTst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeatureTst> getFeatureTst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FeatureTst : {}", id);
        Optional<FeatureTst> featureTst = featureTstRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(featureTst);
    }

    /**
     * {@code DELETE  /feature-tsts/:id} : delete the "id" featureTst.
     *
     * @param id the id of the featureTst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeatureTst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FeatureTst : {}", id);
        featureTstRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
