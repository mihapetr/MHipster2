package com.mihael.mhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Feature.
 */
@Entity
@Table(name = "feature")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Feature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @NotNull
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "features")
    @JsonIgnoreProperties(value = { "mdls", "featureTsts", "user", "features", "overviews" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "features")
    @JsonIgnoreProperties(value = { "parent", "testReports", "features", "project" }, allowSetters = true)
    private Set<FeatureTst> featureTsts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Feature id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Feature name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return this.content;
    }

    public Feature content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Feature user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.removeFeature(this));
        }
        if (projects != null) {
            projects.forEach(i -> i.addFeature(this));
        }
        this.projects = projects;
    }

    public Feature projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Feature addProject(Project project) {
        this.projects.add(project);
        project.getFeatures().add(this);
        return this;
    }

    public Feature removeProject(Project project) {
        this.projects.remove(project);
        project.getFeatures().remove(this);
        return this;
    }

    public Set<FeatureTst> getFeatureTsts() {
        return this.featureTsts;
    }

    public void setFeatureTsts(Set<FeatureTst> featureTsts) {
        if (this.featureTsts != null) {
            this.featureTsts.forEach(i -> i.removeFeature(this));
        }
        if (featureTsts != null) {
            featureTsts.forEach(i -> i.addFeature(this));
        }
        this.featureTsts = featureTsts;
    }

    public Feature featureTsts(Set<FeatureTst> featureTsts) {
        this.setFeatureTsts(featureTsts);
        return this;
    }

    public Feature addFeatureTst(FeatureTst featureTst) {
        this.featureTsts.add(featureTst);
        featureTst.getFeatures().add(this);
        return this;
    }

    public Feature removeFeatureTst(FeatureTst featureTst) {
        this.featureTsts.remove(featureTst);
        featureTst.getFeatures().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feature)) {
            return false;
        }
        return getId() != null && getId().equals(((Feature) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Feature{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
           // ", content='" + getContent() + "'" +
			//", userId='" + getUser().getId() + "'" +
            "}";
    }
}
