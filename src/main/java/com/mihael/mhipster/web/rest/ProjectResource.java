package com.mihael.mhipster.web.rest;

import com.mihael.mhipster.MGenerated;
import com.mihael.mhipster.domain.Feature;
import com.mihael.mhipster.domain.Project;
import com.mihael.mhipster.repository.FeatureRepository;
import com.mihael.mhipster.repository.MDLSRepository;
import com.mihael.mhipster.repository.ProjectRepository;
import com.mihael.mhipster.repository.UserRepository;
import com.mihael.mhipster.security.SecurityUtils;
import com.mihael.mhipster.web.rest.errors.BadRequestAlertException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mihael.mhipster.domain.Project}.
 */
@RestController
@RequestMapping("/api/projects")
@Transactional
public class ProjectResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectResource.class);

    private static final String ENTITY_NAME = "project";
    private final UserRepository userRepository;
    private final FeatureRepository featureRepository;
    private final MDLSRepository mdlsRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectRepository projectRepository;

    @MGenerated
    public ProjectResource(
        ProjectRepository projectRepository,
        UserRepository userRepository,
        FeatureRepository featureRepository,
        MDLSRepository mdlsRepository
    ) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.featureRepository = featureRepository;
        this.mdlsRepository = mdlsRepository;
    }

    /**
     * {@code POST  /projects} : Create a new project.
     *
     * @param project the project to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new project, or with status {@code 400 (Bad Request)} if the project has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) throws URISyntaxException {
        LOG.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            throw new BadRequestAlertException("A new project cannot already have an ID", ENTITY_NAME, "idexists");
        }

        project = createProjectCustom(project);
        project.setId(projectRepository.save(project).getId());
        generateFiles(project);

        return ResponseEntity.created(new URI("/api/projects/" + project.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, project.getId().toString()))
            .body(project);
    }

    //	@PostMapping("/{id}/generate-files")
    //	public ResponseEntity<Project> createProjectFiles(
    //		@PathVariable(value = "id", required = false) final Long id
    //	) throws URISyntaxException {
    //
    //		generateFiles(getProject(id).getBody());
    //	}

    @MGenerated
    @Value("classpath:/mhipster/jdl-template.jdl")
    Resource templateResource;

    @MGenerated
    @Value("classpath:/mhipster/CucumberIT.template")
    Resource cucumberTemplate;

    @MGenerated
    @Value("classpath:/mhipster/mhipster-it-profile.xml")
    Resource pomProfileResource;

    @MGenerated
    Project createProjectCustom(Project project) {
        project.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow());
        project.setFeatures(
            project
                .getFeatures()
                .stream()
                .map(feature -> featureRepository.findById(feature.getId()).orElseThrow())
                .collect(Collectors.toSet())
        );
        project.setMdls(mdlsRepository.findById(project.getMdls().getId()).orElseThrow());
        return project;
    }

    @Async
    void generateFiles(Project project) {
        String jdlTemplateContent = null;
        String cucumberTemplateContent = null;
        String pomProfileContent = null;
        try {
            jdlTemplateContent = new String(templateResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            cucumberTemplateContent = new String(cucumberTemplate.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            pomProfileContent = new String(pomProfileResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        project.generate(jdlTemplateContent, cucumberTemplateContent, pomProfileContent);
        projectRepository.save(project);
    }

    /**
     * {@code PUT  /projects/:id} : Updates an existing project.
     *
     * @param id the id of the project to save.
     * @param project the project to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated project,
     * or with status {@code 400 (Bad Request)} if the project is not valid,
     * or with status {@code 500 (Internal Server Error)} if the project couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Project project
    ) throws URISyntaxException {
        LOG.debug("REST request to update Project : {}, {}", id, project);
        if (project.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, project.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        project = projectRepository.save(project);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, project.getId().toString()))
            .body(project);
    }

    /**
     * {@code PATCH  /projects/:id} : Partial updates given fields of an existing project, field will ignore if it is null
     *
     * @param id the id of the project to save.
     * @param project the project to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated project,
     * or with status {@code 400 (Bad Request)} if the project is not valid,
     * or with status {@code 404 (Not Found)} if the project is not found,
     * or with status {@code 500 (Internal Server Error)} if the project couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Project> partialUpdateProject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Project project
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Project partially : {}, {}", id, project);
        if (project.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, project.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Project> result = projectRepository
            .findById(project.getId())
            .map(existingProject -> {
                if (project.getName() != null) {
                    existingProject.setName(project.getName());
                }
                if (project.getDescription() != null) {
                    existingProject.setDescription(project.getDescription());
                }
                if (project.getCreationTimeStamp() != null) {
                    existingProject.setCreationTimeStamp(project.getCreationTimeStamp());
                }
                if (project.getLocation() != null) {
                    existingProject.setLocation(project.getLocation());
                }

                return existingProject;
            })
            .map(projectRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, project.getId().toString())
        );
    }

    /**
     * {@code GET  /projects} : get all the projects.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projects in body.
     */
    @GetMapping("")
    public List<Project> getAllProjects(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        LOG.debug("REST request to get all Projects");
        if (eagerload) {
            return projectRepository.findAllWithEagerRelationships();
        } else {
            if (filter != null) return filter(filter);
            return projectRepository.findAll();
        }
    }

    @MGenerated
    List<Project> filter(String filter) {
        if (filter.equals("current-user")) {
            return projectRepository.findByUserIsCurrentUser();
        } else return projectRepository.findAll();
    }

    @MGenerated
    @GetMapping("/{id}/download")
    public void downloadZip(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        Project project = projectRepository.findById(id).orElseThrow();
        if (project.getLocation() == null) {
            throw new BadRequestAlertException("Project files not available yet", ENTITY_NAME, "locationIsNull");
        }

        String location = project.getLocation();
        File zipFile = new File(location);
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=files.zip");
        try (InputStream is = new FileInputStream(zipFile); OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        }
    }

    /**
     * {@code GET  /projects/:id} : get the "id" project.
     *
     * @param id the id of the project to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the project, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Project : {}", id);
        Optional<Project> project = projectRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(project);
    }

    /**
     * {@code DELETE  /projects/:id} : delete the "id" project.
     *
     * @param id the id of the project to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Project : {}", id);
        projectRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
