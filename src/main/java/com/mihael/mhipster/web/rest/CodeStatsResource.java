package com.mihael.mhipster.web.rest;

import com.mihael.mhipster.domain.CodeStats;
import com.mihael.mhipster.repository.CodeStatsRepository;
import com.mihael.mhipster.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mihael.mhipster.domain.CodeStats}.
 */
@RestController
@RequestMapping("/api/code-stats")
@Transactional
public class CodeStatsResource {

    private static final Logger LOG = LoggerFactory.getLogger(CodeStatsResource.class);

    private static final String ENTITY_NAME = "codeStats";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodeStatsRepository codeStatsRepository;

    public CodeStatsResource(CodeStatsRepository codeStatsRepository) {
        this.codeStatsRepository = codeStatsRepository;
    }

    /**
     * {@code POST  /code-stats} : Create a new codeStats.
     *
     * @param codeStats the codeStats to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codeStats, or with status {@code 400 (Bad Request)} if the codeStats has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CodeStats> createCodeStats(@RequestBody CodeStats codeStats) throws URISyntaxException {
        LOG.debug("REST request to save CodeStats : {}", codeStats);
        if (codeStats.getId() != null) {
            throw new BadRequestAlertException("A new codeStats cannot already have an ID", ENTITY_NAME, "idexists");
        }
        codeStats = codeStatsRepository.save(codeStats);
        return ResponseEntity.created(new URI("/api/code-stats/" + codeStats.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, codeStats.getId().toString()))
            .body(codeStats);
    }

    /**
     * {@code PUT  /code-stats/:id} : Updates an existing codeStats.
     *
     * @param id the id of the codeStats to save.
     * @param codeStats the codeStats to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeStats,
     * or with status {@code 400 (Bad Request)} if the codeStats is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codeStats couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CodeStats> updateCodeStats(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CodeStats codeStats
    ) throws URISyntaxException {
        LOG.debug("REST request to update CodeStats : {}, {}", id, codeStats);
        if (codeStats.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeStats.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeStatsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        codeStats = codeStatsRepository.save(codeStats);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, codeStats.getId().toString()))
            .body(codeStats);
    }

    /**
     * {@code PATCH  /code-stats/:id} : Partial updates given fields of an existing codeStats, field will ignore if it is null
     *
     * @param id the id of the codeStats to save.
     * @param codeStats the codeStats to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codeStats,
     * or with status {@code 400 (Bad Request)} if the codeStats is not valid,
     * or with status {@code 404 (Not Found)} if the codeStats is not found,
     * or with status {@code 500 (Internal Server Error)} if the codeStats couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CodeStats> partialUpdateCodeStats(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CodeStats codeStats
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CodeStats partially : {}, {}", id, codeStats);
        if (codeStats.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codeStats.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeStatsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodeStats> result = codeStatsRepository
            .findById(codeStats.getId())
            .map(existingCodeStats -> {
                if (codeStats.getInstructions() != null) {
                    existingCodeStats.setInstructions(codeStats.getInstructions());
                }
                if (codeStats.getBranches() != null) {
                    existingCodeStats.setBranches(codeStats.getBranches());
                }
                if (codeStats.getLines() != null) {
                    existingCodeStats.setLines(codeStats.getLines());
                }
                if (codeStats.getMethods() != null) {
                    existingCodeStats.setMethods(codeStats.getMethods());
                }
                if (codeStats.getClasses() != null) {
                    existingCodeStats.setClasses(codeStats.getClasses());
                }
                if (codeStats.getDeadInstructions() != null) {
                    existingCodeStats.setDeadInstructions(codeStats.getDeadInstructions());
                }
                if (codeStats.getDeadBranches() != null) {
                    existingCodeStats.setDeadBranches(codeStats.getDeadBranches());
                }
                if (codeStats.getDeadLines() != null) {
                    existingCodeStats.setDeadLines(codeStats.getDeadLines());
                }
                if (codeStats.getDeadMethods() != null) {
                    existingCodeStats.setDeadMethods(codeStats.getDeadMethods());
                }
                if (codeStats.getDeadClasses() != null) {
                    existingCodeStats.setDeadClasses(codeStats.getDeadClasses());
                }

                return existingCodeStats;
            })
            .map(codeStatsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, codeStats.getId().toString())
        );
    }

    /**
     * {@code GET  /code-stats} : get all the codeStats.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codeStats in body.
     */
    @GetMapping("")
    public List<CodeStats> getAllCodeStats(@RequestParam(name = "filter", required = false) String filter) {
        if ("featuretst-is-null".equals(filter)) {
            LOG.debug("REST request to get all CodeStatss where featureTst is null");
            return StreamSupport.stream(codeStatsRepository.findAll().spliterator(), false)
                .filter(codeStats -> codeStats.getFeatureTst() == null)
                .toList();
        }

        if ("overview-is-null".equals(filter)) {
            LOG.debug("REST request to get all CodeStatss where overview is null");
            return StreamSupport.stream(codeStatsRepository.findAll().spliterator(), false)
                .filter(codeStats -> codeStats.getOverview() == null)
                .toList();
        }
        LOG.debug("REST request to get all CodeStats");
        return codeStatsRepository.findAll();
    }

    /**
     * {@code GET  /code-stats/:id} : get the "id" codeStats.
     *
     * @param id the id of the codeStats to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codeStats, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CodeStats> getCodeStats(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CodeStats : {}", id);
        Optional<CodeStats> codeStats = codeStatsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(codeStats);
    }

    /**
     * {@code DELETE  /code-stats/:id} : delete the "id" codeStats.
     *
     * @param id the id of the codeStats to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodeStats(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CodeStats : {}", id);
        codeStatsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
