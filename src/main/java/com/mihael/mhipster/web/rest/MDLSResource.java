package com.mihael.mhipster.web.rest;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.MDLS;
import com.mihael.mhipster.repository.MDLSRepository;
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
 * REST controller for managing {@link com.mihael.mhipster.domain.MDLS}.
 */
@RestController
@RequestMapping("/api/mdls")
@Transactional
public class MDLSResource {

    private static final Logger LOG = LoggerFactory.getLogger(MDLSResource.class);

    private static final String ENTITY_NAME = "mDLS";
    private final UserRepository userRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MDLSRepository mDLSRepository;

    public MDLSResource(MDLSRepository mDLSRepository, UserRepository userRepository) {
        this.mDLSRepository = mDLSRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /mdls} : Create a new mDLS.
     *
     * @param mDLS the mDLS to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mDLS, or with status {@code 400 (Bad Request)} if the mDLS has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MDLS> createMDLS(@Valid @RequestBody MDLS mDLS) throws URISyntaxException {
        LOG.debug("REST request to save MDLS : {}", mDLS);
        if (mDLS.getId() != null) {
            throw new BadRequestAlertException("A new mDLS cannot already have an ID", ENTITY_NAME, "idexists");
        }
        createMDLSCustom(mDLS);
        mDLS = mDLSRepository.save(mDLS);
        return ResponseEntity.created(new URI("/api/mdls/" + mDLS.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, mDLS.getId().toString()))
            .body(mDLS);
    }

    @MGenerated
    void createMDLSCustom(MDLS mDLS) {
        mDLS.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());
    }

    /**
     * {@code PUT  /mdls/:id} : Updates an existing mDLS.
     *
     * @param id   the id of the mDLS to save.
     * @param mDLS the mDLS to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mDLS,
     * or with status {@code 400 (Bad Request)} if the mDLS is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mDLS couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MDLS> updateMDLS(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody MDLS mDLS)
        throws URISyntaxException {
        LOG.debug("REST request to update MDLS : {}, {}", id, mDLS);
        if (mDLS.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mDLS.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mDLSRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mDLS = mDLSRepository.save(mDLS);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mDLS.getId().toString()))
            .body(mDLS);
    }

    /**
     * {@code PATCH  /mdls/:id} : Partial updates given fields of an existing mDLS, field will ignore if it is null
     *
     * @param id   the id of the mDLS to save.
     * @param mDLS the mDLS to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mDLS,
     * or with status {@code 400 (Bad Request)} if the mDLS is not valid,
     * or with status {@code 404 (Not Found)} if the mDLS is not found,
     * or with status {@code 500 (Internal Server Error)} if the mDLS couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MDLS> partialUpdateMDLS(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MDLS mDLS
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MDLS partially : {}, {}", id, mDLS);
        if (mDLS.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mDLS.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mDLSRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MDLS> result = mDLSRepository
            .findById(mDLS.getId())
            .map(existingMDLS -> {
                if (mDLS.getBaseConfig() != null) {
                    existingMDLS.setBaseConfig(mDLS.getBaseConfig());
                }
                if (mDLS.getContent() != null) {
                    existingMDLS.setContent(mDLS.getContent());
                }

                return existingMDLS;
            })
            .map(mDLSRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mDLS.getId().toString())
        );
    }

    /**
     * {@code GET  /mdls} : get all the mDLS.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mDLS in body.
     */
    @GetMapping("")
    public List<MDLS> getAllMDLS(@RequestParam(name = "filter", required = false) String filter) {
        if (filter.contains("project-is-null")) {
            LOG.debug("REST request to get all MDLSs where project is null");
            return StreamSupport.stream(mDLSRepository.findByUserIsCurrentUser().spliterator(), false)
                .filter(mDLS -> mDLS.getProject() == null)
                .toList();
        } else if (filter != null) {
            return filter(filter);
        }
        LOG.debug("REST request to get all MDLS");
        return mDLSRepository.findAll();
    }

    @MGenerated
    List<MDLS> filter(String filter) {
        if (filter.contains("current-user")) return mDLSRepository.findByUserIsCurrentUser();
        else throw new BadRequestAlertException("Invalid filter", ENTITY_NAME, "invalidfilter");
    }

    /**
     * {@code GET  /mdls/:id} : get the "id" mDLS.
     *
     * @param id the id of the mDLS to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mDLS, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MDLS> getMDLS(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MDLS : {}", id);
        Optional<MDLS> mDLS = mDLSRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mDLS);
    }

    /**
     * {@code DELETE  /mdls/:id} : delete the "id" mDLS.
     *
     * @param id the id of the mDLS to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMDLS(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MDLS : {}", id);
        MDLS mdls = mDLSRepository.findById(id).orElseThrow();
        mdls.setUser(null);
        mdls.setProject(null);
        mDLSRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
