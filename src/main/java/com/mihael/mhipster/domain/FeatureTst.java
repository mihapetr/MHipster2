package com.mihael.mhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mihael.mhipster.MGenerated;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A FeatureTst.
 */
@Entity
@Table(name = "feature_tst")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeatureTst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @JsonIgnoreProperties(value = { "overview", "featureTst" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
    @NotNull
    @JoinColumn(unique = true)
    private CodeStats parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "featureTst", cascade = { CascadeType.REMOVE })
    @JsonIgnoreProperties(value = { "featureTst" }, allowSetters = true)
    private Set<TestReport> testReports = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_feature_tst__feature",
        joinColumns = @JoinColumn(name = "feature_tst_id"),
        inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    @JsonIgnoreProperties(value = { "user", "projects", "featureTsts" }, allowSetters = true)
    private Set<Feature> features = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "mdls", "featureTsts", "user", "features", "overviews" }, allowSetters = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeatureTst id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public FeatureTst date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public CodeStats getParent() {
        return this.parent;
    }

    public void setParent(CodeStats codeStats) {
        this.parent = codeStats;
    }

    public FeatureTst parent(CodeStats codeStats) {
        this.setParent(codeStats);
        return this;
    }

    public Set<TestReport> getTestReports() {
        return this.testReports;
    }

    public void setTestReports(Set<TestReport> testReports) {
        if (this.testReports != null) {
            this.testReports.forEach(i -> i.setFeatureTst(null));
        }
        if (testReports != null) {
            testReports.forEach(i -> i.setFeatureTst(this));
        }
        this.testReports = testReports;
    }

    public FeatureTst testReports(Set<TestReport> testReports) {
        this.setTestReports(testReports);
        return this;
    }

    public FeatureTst addTestReport(TestReport testReport) {
        this.testReports.add(testReport);
        testReport.setFeatureTst(this);
        return this;
    }

    public FeatureTst removeTestReport(TestReport testReport) {
        this.testReports.remove(testReport);
        testReport.setFeatureTst(null);
        return this;
    }

    public Set<Feature> getFeatures() {
        return this.features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    public FeatureTst features(Set<Feature> features) {
        this.setFeatures(features);
        return this;
    }

    public FeatureTst addFeature(Feature feature) {
        this.features.add(feature);
        return this;
    }

    public FeatureTst removeFeature(Feature feature) {
        this.features.remove(feature);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public FeatureTst project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeatureTst)) {
            return false;
        }
        return getId() != null && getId().equals(((FeatureTst) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeatureTst{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", testReports=" + getTestReports() +
            ", project=" + getProject().getId() +
            ", codeStats=" + getParent() +
            "}";
    }

    /**
     * Calculate the contribution percentage of Jhipster based on two TestReports.
     * Calculate the dead code percentage in the project based on two TestReports.
     */
    @MGenerated
    void generateStats() {}
}
