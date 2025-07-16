package com.mihael.mhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mihael.mhipster.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_time_stamp")
    private ZonedDateTime creationTimeStamp;

    @Column(name = "location")
    private String location;

    @JsonIgnoreProperties(value = { "user", "project" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MDLS mdls;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = { CascadeType.REMOVE })
    @JsonIgnoreProperties(value = { "parent", "testReports", "features", "project" }, allowSetters = true)
    private Set<FeatureTst> featureTsts = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_project__feature",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    @JsonIgnoreProperties(value = { "user", "projects", "featureTsts" }, allowSetters = true)
    private Set<Feature> features = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "projects")
    @JsonIgnoreProperties(value = { "parent", "user", "projects" }, allowSetters = true)
    private Set<Overview> overviews = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Project id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Project name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Project description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreationTimeStamp() {
        return this.creationTimeStamp;
    }

    public Project creationTimeStamp(ZonedDateTime creationTimeStamp) {
        this.setCreationTimeStamp(creationTimeStamp);
        return this;
    }

    public void setCreationTimeStamp(ZonedDateTime creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public String getLocation() {
        return this.location;
    }

    public Project location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MDLS getMdls() {
        return this.mdls;
    }

    public void setMdls(MDLS mDLS) {
        this.mdls = mDLS;
    }

    public Project mdls(MDLS mDLS) {
        this.setMdls(mDLS);
        return this;
    }

    public Set<FeatureTst> getFeatureTsts() {
        return this.featureTsts;
    }

    public void setFeatureTsts(Set<FeatureTst> featureTsts) {
        if (this.featureTsts != null) {
            this.featureTsts.forEach(i -> i.setProject(null));
        }
        if (featureTsts != null) {
            featureTsts.forEach(i -> i.setProject(this));
        }
        this.featureTsts = featureTsts;
    }

    public Project featureTsts(Set<FeatureTst> featureTsts) {
        this.setFeatureTsts(featureTsts);
        return this;
    }

    public Project addFeatureTst(FeatureTst featureTst) {
        this.featureTsts.add(featureTst);
        featureTst.setProject(this);
        return this;
    }

    public Project removeFeatureTst(FeatureTst featureTst) {
        this.featureTsts.remove(featureTst);
        featureTst.setProject(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Feature> getFeatures() {
        return this.features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    public Project features(Set<Feature> features) {
        this.setFeatures(features);
        return this;
    }

    public Project addFeature(Feature feature) {
        this.features.add(feature);
        return this;
    }

    public Project removeFeature(Feature feature) {
        this.features.remove(feature);
        return this;
    }

    public Set<Overview> getOverviews() {
        return this.overviews;
    }

    public void setOverviews(Set<Overview> overviews) {
        if (this.overviews != null) {
            this.overviews.forEach(i -> i.removeProject(this));
        }
        if (overviews != null) {
            overviews.forEach(i -> i.addProject(this));
        }
        this.overviews = overviews;
    }

    public Project overviews(Set<Overview> overviews) {
        this.setOverviews(overviews);
        return this;
    }

    public Project addOverview(Overview overview) {
        this.overviews.add(overview);
        overview.getProjects().add(this);
        return this;
    }

    public Project removeOverview(Overview overview) {
        this.overviews.remove(overview);
        overview.getProjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return getId() != null && getId().equals(((Project) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationTimeStamp='" + getCreationTimeStamp() + "'" +
            ", location='" + getLocation() + "'" +
            //", user= " + getUser() +
            //", featureTsts= " + getFeatureTsts() +
			//", features= " + getFeatures() +
			//", mdls= " + getMdls().getId() +
            "}";
    }

    @MGenerated
    static void execute(String directory, String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(directory));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // capture output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            //return process.waitFor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // todo : make a config entity which will define where the new project is generated

    @MGenerated
    public void generate(String jdlTemplateContent, String cucumberTemplateContent, String pomProfileContent) {
        // calculate the directory positions relative to this project
        String projectRoot = System.getProperty("user.dir");
        String parentDir = new File(projectRoot).getParent();
        String projectDirName = "" + getId();
        String userLogin = getUser().getLogin();
        String cleanedName = getName().trim().toLowerCase().replace(" ", "");
        String packageName = "com." + userLogin + "." + cleanedName;
        String projectDir = parentDir + "/" + userLogin + "/" + projectDirName;
        String specificationPath = projectDir + "/specification.jdl";
        String testResourcesDir = projectDir + "/src/test/resources";

        System.out.println(projectDir);
        // make a directory on the server dedicated to the user and their projects
        execute(parentDir, "mkdir", "-p", userLogin);
        execute(parentDir, "mkdir", "-p", userLogin + "/" + projectDirName);

        // make changes to app specification user provided based on JDL extension
        MDLSProcessor.transform(jdlTemplateContent, getMdls().getContent(), cleanedName, packageName, specificationPath);

        // run basic jhipster project generation based on the jdl file
        execute(projectDir, "jhipster", "jdl", "specification.jdl", "--skip-install");
        //execute(projectDir, "echo", "jhipster jdl spec.jdl --skip-install ........ done");

        // modify the domain package based on MDL specification
        MDLSProcessor.modifyDomain(specificationPath, projectDir + "/src/main/java/" + packageName.replace('.', '/') + "/domain");

        // modify pom : add cucumber dependency and mhipster-it profile
        execute(projectDir, "mv", "pom.xml", "backup_pom.xml");
        PomEditor.extend(pomProfileContent, projectDir);

        // configure cucumber : add CucumberIT.java from template
        CucumberSetup.configure(cucumberTemplateContent, projectDir, packageName);

        // generate stepdefs from feature files
        execute(testResourcesDir, "mkdir", "-p", "features");
        execute(testResourcesDir, "mkdir", "-p", "selected_features");
        for (Feature feature : getFeatures()) {
            StepdefGenerator.generateStepdefs(feature.getId(), feature.getContent(), projectDir, packageName);
        }

        // clone testing scripts to the new project
        execute(projectDir, "mkdir", "-p", "mhipster");
        execute(projectRoot, "cp", "test_features.sh", projectDir);

        execute(testResourcesDir + "/features", "sh", "-c", "ls -1 > " + projectDir + "/test_features_selection.txt");

        execute(projectDir, "cp", "pom.xml", "backup_pom.xml");
        execute(projectRoot, "cp", "mhipster/get_jwt.sh", "mhipster/m_generate.sh", "mhipster/post.sh", projectDir + "/mhipster");

        // configure the variable source file for the test_features.sh
        FeatureTestingSetup.configure(
            projectDir,
            "http://localhost:8080", // there should be some kind of domain name service to give this dynamic information
            "./src/main/java/" + packageName.replace(".", "/"),
            getId()
        );

        // compress the project and save the location
        setLocation(Compressor.compress(projectDir));
    }
}
